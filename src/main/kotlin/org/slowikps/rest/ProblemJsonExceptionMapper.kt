package org.slowikps.rest

import org.postgresql.util.PSQLException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import kotlin.reflect.KClass

@Provider
class ProblemJsonExceptionMapper : ExceptionMapper<RuntimeException> {

    //Ugly - exception wrapped
    override fun toResponse(exception: RuntimeException): Response {
        val body = when (val ex = tryExtractSupportedTypes(exception, PSQLException::class.java)) {
            is PSQLException -> {
                if (ex.sqlState == "23505" ) Problem("Bad Request ", 400, "Duplicated value [message: ${ex.serverErrorMessage.detail}]" )
                else Problem("Bad Request ", 400, ex.serverErrorMessage.detail)
            }
            else -> {
                badRequest(exception.message ?: "UNKNOWN")
            }
        }
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(body)
            .build();
    }

    //Hacky hack
    private fun tryExtractSupportedTypes(cause: Throwable?, vararg types: Class<PSQLException>): Throwable? {
        return when {
            cause == null -> {
                null
            }
            types.any{ it == cause::class.java } -> {
                cause
            }
            else -> tryExtractSupportedTypes(cause.cause, *types)
        }
    }

    private fun badRequest(message: String) = Problem("Bad Request ", 400, message)
}