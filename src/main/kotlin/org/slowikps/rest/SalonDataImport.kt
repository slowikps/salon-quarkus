package org.slowikps.rest

import org.apache.commons.io.IOUtils
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.slowikps.model.Appointment
import org.slowikps.model.Client
import org.slowikps.model.Purchase
import org.slowikps.model.Service
import org.slowikps.service.DataImportService
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/admin/import")
class SalonDataImport(
    @Inject
    private val dataImportService: DataImportService
) {

    @Path("client")
    @POST
    @Produces(MediaType.APPLICATION_JSON) //TODO: is this correct output?
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun importClient(@MultipartForm body: MultipartBody) {
        dataImportService.persistClients(fileAsListOf(body, Client.Factory::fromString))
    }

    @Path("appointment")
    @POST
    @Produces(MediaType.APPLICATION_JSON) //TODO: is this correct output?
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun importAppointment(@MultipartForm body: MultipartBody) {
        dataImportService.persistAppointments(fileAsListOf(body, Appointment.Factory::fromString))
    }

    @Path("purchase")
    @POST
    @Produces(MediaType.APPLICATION_JSON) //TODO: is this correct output?
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun importPurchases(@MultipartForm body: MultipartBody) {
        dataImportService.persistPurchases(fileAsListOf(body, Purchase.Factory::fromString))
    }

    @Path("service")
    @POST
    @Produces(MediaType.APPLICATION_JSON) //TODO: is this correct output?
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun importServices(@MultipartForm body: MultipartBody) {
        dataImportService.persistService(fileAsListOf(body, Service.Factory::fromString))
    }

    private fun <T> fileAsListOf(body: MultipartBody, transformer: (String) -> T): List<T> {
        return IOUtils.toString(body.file, StandardCharsets.UTF_8)
            .lines()
            .drop(1)
            .filter { it.isNotBlank() }
            .map(transformer)
    }
}