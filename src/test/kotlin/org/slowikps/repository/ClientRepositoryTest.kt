package org.slowikps.repository

import io.quarkus.arc.ArcUndeclaredThrowableException
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slowikps.config.DatabaseResource
import org.slowikps.model.Client
import org.slowikps.model.Status
import org.slowikps.util.CleanDBeforeEach
import java.util.UUID
import javax.inject.Inject
import javax.validation.ConstraintViolationException

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
class ClientRepositoryTest : CleanDBeforeEach() {

    @Inject
    private lateinit var clientRepository: ClientRepository

    val testClient = Client(
        UUID.randomUUID(),
        "Pawel",
        "Slowinski",
        "my-email@gmail.com",
        "1234",
        "Male",
        Status.ACTIVE
    )

    @Test
    fun `findById should return null for non-existing client`() {
        Assert.assertNull(clientRepository.findById(UUID.randomUUID()))
    }

    @Test
    fun `find existing client should return correct value`() {
        clientRepository.persist(testClient)
        Assert.assertEquals(
            testClient,
            clientRepository.findById(testClient.id!!)
        )
    }

    @Test
    fun `reinserting the same client should cause an exception`() {
        clientRepository.persist(testClient)
        Assertions.assertThrows(ArcUndeclaredThrowableException::class.java) {
            clientRepository.persist(testClient)
        }
    }
}