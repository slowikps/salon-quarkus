package org.slowikps.repository

import org.slowikps.model.Purchase
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class PurchaseRepository {

    @Inject
    private lateinit var em: EntityManager

    @Transactional
    fun persist(purchase: Purchase) {
        em.persist(purchase)
    }

    @Transactional
    fun persistAll(purchases: List<Purchase>) {
        purchases.forEach { persist(it) }
    }
}