package org.slowikps.repository

import org.slowikps.model.Client
import org.slowikps.model.ClientView
import java.time.OffsetDateTime
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

    fun getActiveClientsWithMostPoints(limit: Int, from: OffsetDateTime): List<ClientView> {
        return em.createNamedQuery("Client.getMostLoyal", ClientView::class.java)
            .setMaxResults(limit)
            .setParameter("from", from)
            .resultList
    }
}