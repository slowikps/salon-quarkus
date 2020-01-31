package org.slowikps.service

import org.slowikps.model.Client
import org.slowikps.model.ClientPut
import org.slowikps.model.ClientView
import org.slowikps.model.Status
import org.slowikps.repository.ClientRepository
import java.time.OffsetDateTime
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.core.Response

@ApplicationScoped
class ClientService {

    @Inject
    private lateinit var clientRepository: ClientRepository

    fun getActiveClientsWithMostPoints(limit: Int, from: OffsetDateTime): List<ClientView> {
        return clientRepository.getActiveClientsWithMostPoints(limit, from)
    }

    fun findById(id: UUID): Client? {
        return clientRepository.findById(id)
    }

    @Transactional
    fun deactivate(id: UUID): Client? {
        return findById(id)?.let {
            it.status = Status.DELETED
            it
        }
    }

    @Transactional
    fun upsert(id: UUID, clientPut: ClientPut): Response.Status {
        val fromDb =  clientRepository.findById(id)
        return if(fromDb == null) {
            clientRepository.persist(clientPut.toClient(id))
            Response.Status.CREATED
        } else {
            fromDb.firstName = clientPut.firstName
            fromDb.lastName = clientPut.lastName
            fromDb.email = clientPut.email
            fromDb.phone = clientPut.phone
            fromDb.gender = clientPut.gender
            fromDb.status = clientPut.status
            Response.Status.NO_CONTENT
        }
    }
}