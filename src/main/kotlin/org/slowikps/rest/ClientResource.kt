package org.slowikps.rest

import org.slowikps.model.Client
import org.slowikps.model.ClientPut
import org.slowikps.model.ClientView
import org.slowikps.rest.util.ResponsePage
import org.slowikps.service.ClientService
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.UUID
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/client")
class ClientResource {

    @Inject
    private lateinit var clientService: ClientService

    @Context
    private lateinit var uriInfo: UriInfo

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Path("findMostLoyal")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun getMostLoyalClient(
        @QueryParam("limit") limit: Int,
        @QueryParam("from") from: String
    ): ResponsePage<ClientView> {
        return ResponsePage(
            clientService.getActiveClientsWithMostPoints(
                limit, dateFormat.parse(from).toInstant().atOffset(ZoneOffset.UTC)
            )
        )
    }

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun getById(
        @PathParam("id") id: UUID
    ): Client {
        return clientService.findById(id) ?: throw NotFoundException()
    }

    //Alternative PUT on /client/{id}/enablement
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    fun delete(
        @PathParam("id") id: UUID
    ) {
        clientService.deactivate(id) ?: throw NotFoundException()
    }

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    fun upsert(
        @PathParam("id") id: UUID,
        client: ClientPut
    ): Response? {
        return if (clientService.upsert(id, client) == Response.Status.CREATED) {
            Response.created(uriInfo.absolutePath)
                .build();
        } else {
            Response.noContent()
                .build();
        }
    }
}