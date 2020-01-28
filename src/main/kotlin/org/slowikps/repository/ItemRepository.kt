package org.slowikps.repository

import org.slowikps.model.Item
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class ItemRepository {

    @Inject
    private lateinit var em: EntityManager

    @Transactional
    fun persist(item: Item) {
        em.persist(item)
    }

    @Transactional
    fun persistAll(items: List<Item>) {
        items.forEach { persist(it) }
    }
}