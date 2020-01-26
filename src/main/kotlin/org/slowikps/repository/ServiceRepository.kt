package org.slowikps.repository

import org.slowikps.model.Service
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class ServiceRepository {

    @Inject
    private lateinit var em: EntityManager

    @Transactional
    fun persist(service: Service) {
        em.persist(service)
    }

    @Transactional
    fun persistAll(services: List<Service>) {
        services.forEach { persist(it) }
    }
}