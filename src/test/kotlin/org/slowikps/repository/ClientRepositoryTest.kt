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
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
class ClientRepositoryTest : CleanDBeforeEach() {

    @Inject
    private lateinit var clientRepository: ClientRepository

    private val activeClient1 = Client(
        UUID.randomUUID(),
        "Pawel",
        "Slowinski",
        "my-email@gmail.com",
        "1234",
        "Male",
        Status.ACTIVE
    )

    private val blockedClient = Client(
        UUID.randomUUID(),
        "bcName",
        "bcLastName",
        "bc-email@gmail.com",
        "342",
        "Female",
        Status.BLOCKED
    )

    @Test
    fun `findById should return null for non-existing client`() {
        Assert.assertNull(clientRepository.findById(UUID.randomUUID()))
    }

    @Test
    fun `find existing client should return correct value`() {
        clientRepository.persist(activeClient1)
        Assert.assertEquals(
            activeClient1,
            clientRepository.findById(activeClient1.id)
        )
    }

    @Test
    fun `find existing blocked client should return correct value`() {
        clientRepository.persist(blockedClient)
        Assert.assertEquals(
            blockedClient,
            clientRepository.findById(blockedClient.id)
        )
    }

    @Test
    fun `reinserting the same client should cause an exception`() {
        clientRepository.persist(activeClient1)
        Assertions.assertThrows(ArcUndeclaredThrowableException::class.java) {
            clientRepository.persist(activeClient1)
        }
    }

    @Test
    fun `blocked client should not be returned by getActiveClientsWithMostPoints`() {
        clientRepository.persist(blockedClient)
        Assert.assertEquals(0,
            clientRepository.getActiveClientsWithMostPoints(10, OffsetDateTime.now().minusYears(100))
                .size
        )
    }
}