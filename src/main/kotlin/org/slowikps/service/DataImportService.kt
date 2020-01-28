package org.slowikps.service

import org.slowikps.model.Appointment
import org.slowikps.model.Client
import org.slowikps.model.Item
import org.slowikps.repository.AppointmentRepository
import org.slowikps.repository.ClientRepository
import org.slowikps.repository.ItemRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DataImportService {

    @Inject
    private lateinit var clientRepository: ClientRepository
    @Inject
    private lateinit var appointmentRepository: AppointmentRepository
    @Inject
    private lateinit var itemRepository: ItemRepository

    fun persistClients(clients: List<Client>) {
        clientRepository.persistAll(clients)
    }

    fun persistAppointments(appointments: List<Appointment>) {
        appointmentRepository.persistAll(appointments)
    }

    fun persistService(items: List<Item>) {
        itemRepository.persistAll(items)
    }
}