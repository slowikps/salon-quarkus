package org.slowikps.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.slowikps.config.DatabaseResource
import org.slowikps.model.ClientPut
import org.slowikps.model.Status
import org.slowikps.util.CleanDBeforeEach
import java.util.UUID

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
open class ClientResourceTest : CleanDBeforeEach() {

    val nonExistingUUID: UUID = UUID.fromString("EF51943B-0F6F-4FFF-85BD-E6BFE0935102")

    val clientPut = ClientPut(
        "FirstName",
        "LastName",
        "email@email.com",
        "123",
        "Female",
        Status.ACTIVE
    )

    @Test
    fun `get client by Id should return 404 for non existing client`() {
        RestAssured
            .`when`()
                .get("/client/$nonExistingUUID")
            .then()
                .statusCode(404)
    }

    @Test
    fun `PUT of non existing client should return 201 and location header`() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(clientPut)
            .`when`()
                .put("/client/$nonExistingUUID")
            .then()
                .statusCode(201)
                .header("location", "http://0.0.0.0:8081/client/ef51943b-0f6f-4fff-85bd-e6bfe0935102")
    }

    @Test
    fun `PUT of existing client should return 201 and properly update fields location header`() {
        `PUT of non existing client should return 201 and location header`()

        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(clientPut.copy(firstName = "FirstNameUpdated"))
            .`when`()
                .put("/client/$nonExistingUUID")
            .then()
                .statusCode(204)

        RestAssured
            .`when`()
                .get("/client/$nonExistingUUID")
            .then()
                .statusCode(200)
                .body("firstName", equalTo("FirstNameUpdated"))
                .body("lastName", equalTo("LastName"))
                .body("email", equalTo("email@email.com"))
                .body("phone", equalTo("123"))
                .body("gender", equalTo("Female"))
                .body("status", equalTo("ACTIVE"))
    }

    @Test
    fun `deleting client by Id should return 404 for non existing client`() {
        RestAssured
            .`when`()
                .delete("/client/$nonExistingUUID")
            .then()
                .statusCode(404)
    }

    @Test
    fun `deleting existing client should make it unreachable via get`() {
        `PUT of non existing client should return 201 and location header`()

        RestAssured
            .`when`()
                .delete("/client/$nonExistingUUID")
            .then()
                .statusCode(204)

        RestAssured
            .`when`()
                .get("/client/$nonExistingUUID")
            .then()
                .statusCode(404)
    }
}