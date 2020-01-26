package org.slowikps.repository

import org.slowikps.model.Appointment
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ApplicationScoped
class AppointmentRepository {

    @Inject
    private lateinit var em: EntityManager

    @Transactional
    fun persist(appointment: Appointment) {
        em.persist(appointment)
    }

    @Transactional
    fun persistAll(appointments: List<Appointment>) {
        appointments.forEach { persist(it) }
    }
}