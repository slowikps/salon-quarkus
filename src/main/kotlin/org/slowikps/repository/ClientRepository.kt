package org.slowikps.repository

import org.slowikps.model.Client
import org.slowikps.model.ClientView
import org.slowikps.model.Status
import java.time.OffsetDateTime
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class ClientRepository {

    @Inject
    private lateinit var em: EntityManager

    @Transactional
    fun persist(client: Client) {
        em.persist(client)
    }

    @Transactional
    fun persistAll(clients: List<Client>) {
        clients.forEach { persist(it) }
    }

    fun findById(id: UUID): Client? {
        return em.find(Client::class.java, id)
            ?.takeIf { it.status != Status.DELETED }
    }

    fun getActiveClientsWithMostPoints(limit: Int, from: OffsetDateTime): List<ClientView> {
        return em.createNamedQuery("Client.getMostLoyal", ClientView::class.java)
            .setMaxResults(limit)
            .setParameter("from", from)
            .resultList
    }
}