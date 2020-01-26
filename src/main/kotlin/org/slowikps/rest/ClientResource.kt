package org.slowikps.rest

import org.slowikps.model.Client
import org.slowikps.model.ClientView
import org.slowikps.service.ClientService
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.UUID
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/client")
class ClientResource {

    @Inject
    private lateinit var clientService: ClientService

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @Path("findMostLoyal")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun getMostLoyalClient(
        @QueryParam("limit") limit: Int,
        @QueryParam("from") from: String
    ): List<ClientView> {
        return clientService.getActiveClientsWithMostPoints(
            limit, dateFormat.parse(from).toInstant().atOffset(ZoneOffset.UTC)
        )
    }

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun getById(
        @PathParam("id") id: UUID
    ): Client? {
        return clientService.getById(id)
    }
}