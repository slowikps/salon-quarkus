package org.slowikps.rest.util

import org.jboss.resteasy.api.validation.ResteasyViolationException
import org.postgresql.util.PSQLException
import java.text.ParseException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ProblemJsonValidationException : ExceptionMapper<ResteasyViolationException> {
    override fun toResponse(exception: ResteasyViolationException): Response {
        return Response.status(400)
            .entity(Problem("Bad Request ", 400, exception.message ?: "UNKNOWN"))
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}

@Provider
class ProblemJsonExceptionMapper : ExceptionMapper<RuntimeException> {

    //Ugly - exception wrapped
    override fun toResponse(exception: RuntimeException): Response {
        val problem: Problem = when (val ex =
            tryExtractSupportedTypes(exception, PSQLException::class.java, NotFoundException::class.java, ParseException::class.java)) {
            is PSQLException -> {
                if (ex.sqlState == "23505") Problem(
                    "Bad Request",
                    400,
                    "Duplicated value [message: ${ex.serverErrorMessage.detail}]"
                )
                else Problem(
                    "Bad Request",
                    400,
                    ex.serverErrorMessage.detail
                )
            }
            is ParseException -> {
                Problem(
                    "Bad Request",
                    400,
                    ex.message ?: "UNKNOWN"
                )
            }
            is NotFoundException -> {
                Problem(
                    "Not Found",
                    404,
                    ex.message ?: "UNKNOWN"
                )
            }
            else -> {
                badRequest(exception.message ?: "UNKNOWN")
            }
        }
        return Response.status(problem.status)
            .entity(problem)
            .type(MediaType.APPLICATION_JSON)
            .build()
    }

    //Hacky hack, checked exception are wrapped
    private fun tryExtractSupportedTypes(cause: Throwable?, vararg types: Class<out Exception>): Throwable? {
        return when {
            cause == null -> {
                null
            }
            types.any { it == cause::class.java } -> {
                cause
            }
            else -> tryExtractSupportedTypes(cause.cause, *types)
        }
    }

    private fun badRequest(message: String) =
        Problem("Bad Request ", 400, message)
}