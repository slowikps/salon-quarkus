package org.slowikps.rest

import org.apache.commons.io.IOUtils
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.slowikps.model.Client
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

    @POST
    @Produces(MediaType.APPLICATION_JSON) //TODO: is this correct output?
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun import(@MultipartForm body: MultipartBody) {
        val clients: List<Client> = IOUtils.toString(body.file, StandardCharsets.UTF_8)
            .lines()
            .drop(1)
            .filter { it.isNotBlank() }
            .map { Client.fromString(it) }

        dataImportService.persist(clients)
        println(clients)
        "hello"
    }
}