package org.slowikps.service

import org.slowikps.model.ClientView
import org.slowikps.repository.ClientRepository
import java.time.OffsetDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ClientService {

    @Inject
    private lateinit var clientRepository: ClientRepository

    fun getActiveClientsWithMostPoints(limit: Int, from: OffsetDateTime): List<ClientView> {
        return clientRepository.getActiveClientsWithMostPoints(limit, from)
    }
}