package org.slowikps.service

import org.slowikps.model.Client
import org.slowikps.repository.ClientRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DataImportService(
    @Inject
    val clientRepository: ClientRepository
) {

    fun persist(clients: List<Client>) {
        clientRepository.persistAll(clients)
    }
}