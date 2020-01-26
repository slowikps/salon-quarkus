package org.slowikps.rest

import org.slowikps.model.ClientView
import org.slowikps.service.ClientService
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/client")
class ClientResource {

    @Inject
    private lateinit var clientService: ClientService

    @Path("findMostLoyal")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    fun getMostLoyalClient(
        @QueryParam("limit") limit: Int = 50,
        @QueryParam("from") from: OffsetDateTime
    ): List<ClientView> {
        return clientService.getActiveClientsWithMostPoints(limit, from)
    }
}