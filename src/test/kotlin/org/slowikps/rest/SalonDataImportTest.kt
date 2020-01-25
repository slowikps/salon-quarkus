package org.slowikps.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import org.slowikps.config.DatabaseResource

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
open class SalonDataImportTest {

    @Test
    fun testClientsImport() {
        // Given
        // Given
        val bytes: ByteArray = IOUtils.toByteArray({}.javaClass.getResourceAsStream("/clients.csv"))

        given()
            .multiPart("file", "file", bytes)
            .formParam("type", "client")
          .`when`()
            .post("/admin/import/client")
          .then()
             .statusCode(204)
        // .body(`is`("hello"))
    }

}