package org.slowikps.service

import org.slowikps.model.Appointment
import org.slowikps.model.Client
import org.slowikps.model.Purchase
import org.slowikps.model.Service
import org.slowikps.repository.AppointmentRepository
import org.slowikps.repository.ClientRepository
import org.slowikps.repository.PurchaseRepository
import org.slowikps.repository.ServiceRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DataImportService {

    @Inject
    private lateinit var clientRepository: ClientRepository
    @Inject
    private lateinit var appointmentRepository: AppointmentRepository
    @Inject
    private lateinit var purchaseRepository: PurchaseRepository
    @Inject
    private lateinit var serviceRepository: ServiceRepository

    fun persistClients(clients: List<Client>) {
        clientRepository.persistAll(clients)
    }

    fun persistAppointments(appointments: List<Appointment>) {
        appointmentRepository.persistAll(appointments)
    }

    fun persistPurchases(purchases: List<Purchase>) {
        purchaseRepository.persistAll(purchases)
    }

    fun persistService(services: List<Service>) {
        serviceRepository.persistAll(services)
    }
}