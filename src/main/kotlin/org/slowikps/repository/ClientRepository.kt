package org.slowikps.repository

import org.slowikps.model.Client
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class ClientRepository(
    @Inject
    private val em: EntityManager
) {

    @Transactional
    fun persist(client: Client) {
        em.persist(client)
    }

    @Transactional
    fun persistAll(clients: List<Client>) {
        clients.forEach { persist(it) }
    }
}