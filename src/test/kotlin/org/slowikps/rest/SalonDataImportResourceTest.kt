package org.slowikps.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import org.slowikps.config.DatabaseResource
import org.slowikps.util.CleanDBeforeEach

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
open class SalonDataImportResourceTest : CleanDBeforeEach() {

    @Test
    fun testAllImports() {
        sendFileToImportEndpoint("client")
        sendFileToImportEndpoint("appointment")
        sendFileToImportEndpoint("purchase")
        sendFileToImportEndpoint("service")
    }

    private fun sendFileToImportEndpoint(name: String) {
        val bytes: ByteArray = IOUtils.toByteArray({}.javaClass.getResourceAsStream("/${name}s.csv"))

        given()
            .multiPart("file", "file", bytes)
        .`when`()
            .post("/admin/import/$name")
        .then()
            .statusCode(204)
    }
}