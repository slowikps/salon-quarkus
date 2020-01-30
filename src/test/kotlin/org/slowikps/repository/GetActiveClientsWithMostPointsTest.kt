package org.slowikps.repository

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import org.slowikps.config.DatabaseResource
import org.slowikps.model.Appointment
import org.slowikps.model.Client
import org.slowikps.model.ClientView
import org.slowikps.model.Item
import org.slowikps.model.Status
import org.slowikps.model.Type.PURCHASE
import org.slowikps.model.Type.SERVICE
import org.slowikps.util.CleanDBeforeEach
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
class GetActiveClientsWithMostPointsTest : CleanDBeforeEach() {
    @Inject
    private lateinit var clientRepository: ClientRepository
    @Inject
    private lateinit var appointmentRepository: AppointmentRepository
    @Inject
    private lateinit var itemRepository: ItemRepository

    //CLIENT 1
    private val activeClient1 =
        Client(UUID.randomUUID(), "Name1", "LastName1", "email1@gmail.com", "1111", "Female", Status.ACTIVE)

    private val appointmentA1C1 =
        Appointment(UUID.randomUUID(), activeClient1.id, "2020-01-01".toODT(), "2020-01-02".toODT())
    private val service1A1C1 =
        Item(UUID.randomUUID(), appointmentA1C1.id, "service1A1C1", BigDecimal(10), SERVICE, 10)
    private val service2A1C1 =
        Item(UUID.randomUUID(), appointmentA1C1.id, "service2A1C1", BigDecimal(20), SERVICE, 20)
    private val purchase1A1C1 =
        Item(UUID.randomUUID(), appointmentA1C1.id, "purchase1A1C1", BigDecimal(20), PURCHASE, 30)

    private val appointmentA2C1 =
        Appointment(UUID.randomUUID(), activeClient1.id, "2020-02-01".toODT(), "2020-02-02".toODT())
    private val purchase1A2C1 =
        Item(UUID.randomUUID(), appointmentA2C1.id, "purchase1A2C1", BigDecimal(20), PURCHASE, 30)

    private val appointmentA3C1 =
        Appointment(UUID.randomUUID(), activeClient1.id, "2020-03-01".toODT(), "2020-03-02".toODT())
    private val service1A3C1 =
        Item(UUID.randomUUID(), appointmentA3C1.id, "service1A3C1", BigDecimal(30), SERVICE, 40)

    //CLIENT 2
    private val activeClient2 =
        Client(UUID.randomUUID(), "Name2", "LastName2", "email2@gmail.com", "2222", "Male", Status.ACTIVE)

    private val appointmentA1C2 =
        Appointment(UUID.randomUUID(), activeClient2.id, "2020-01-01".toODT(), "2020-01-02".toODT())

    private val appointmentA2C2 =
        Appointment(UUID.randomUUID(), activeClient2.id, "2020-01-03".toODT(), "2020-01-04".toODT())
    private val purchase1A2C2 =
        Item(UUID.randomUUID(), appointmentA2C2.id, "purchase1A2C2", BigDecimal(20), PURCHASE, 180)

    private val appointmentA3C2 =
        Appointment(UUID.randomUUID(), activeClient2.id, "2020-01-11".toODT(), "2020-01-11".toODT())
    private val service1A3C2 =
        Item(UUID.randomUUID(), appointmentA3C2.id, "service1A3C2", BigDecimal(20), SERVICE, 1)

    //CLIENT 3
    private val activeClient3 =
        Client(UUID.randomUUID(), "Name3", "LastName3", "email3@gmail.com", "3333", "Male", Status.ACTIVE)

    private val appointmentA1C3 =
        Appointment(UUID.randomUUID(), activeClient3.id, "2020-01-05".toODT(), "2020-01-06".toODT())
    private val service1A1C3 =
        Item(UUID.randomUUID(), appointmentA1C3.id, "purchase1A1C3", BigDecimal(20), PURCHASE, 190)

    //CLIENT Blocked
    private val blockedClient =
        Client(UUID.randomUUID(), "bcName", "bcLastName", "bc-email@gmail.com", "342", "Female", Status.BLOCKED)

    private val appointmentA1CBlocked =
        Appointment(UUID.randomUUID(), blockedClient.id, "2020-01-03".toODT(), "2020-01-04".toODT())
    private val service1A1CBlocked =
        Item(UUID.randomUUID(), appointmentA1CBlocked.id, "service1A1CBlocked", BigDecimal(30), SERVICE, 4000)

    private fun String.toODT(): OffsetDateTime {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.parse(this).toInstant().atOffset(ZoneOffset.UTC).plusSeconds(10)
    }

    @Suppress("PrivatePropertyName")
    private val `01-01-2020` = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)

    @Test
    fun `client with 0 appointments in a given period are not returned`() {
        clientRepository.persist(activeClient1)
        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`)).all {
            hasSize(0)
        }
    }

    @Test
    fun `client with 1 appointments without any purchase nor service is not returned`() {
        clientRepository.persist(activeClient1)
        appointmentRepository.persist(appointmentA1C1)
        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`)).all {
            hasSize(0)
        }
    }

    @Test
    fun `client1 should have 130 points in total`() {
        persistAllDataForClient1()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`))
            .containsExactly(activeClient1.toClientView(130))
    }

    @Test
    fun `client1 should have 70 points from february`() {
        persistAllDataForClient1()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusDays(31)))
            .containsExactly(activeClient1.toClientView(70))
    }

    @Test
    fun `client1 should have 40 points from march`() {
        persistAllDataForClient1()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusMonths(2)))
            .containsExactly(activeClient1.toClientView(40))
    }

    @Test
    fun `client2 has 181 points from Jan and should be returned before client1`() {
        persistAllDataForClient1()
        persistAllDataForClient2()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`))
            .containsExactly(
                activeClient2.toClientView(181),
                activeClient1.toClientView(130)
            )
    }

    @Test
    fun `client2 has 1 points from 10th of Jan and should be returned after client1`() {
        persistAllDataForClient1()
        persistAllDataForClient2()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusDays(10)))
            .containsExactly(
                activeClient1.toClientView(70),
                activeClient2.toClientView(1)
            )
    }

    @Test
    fun `all clients from 01-01-2020 limit 1`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(1, `01-01-2020`))
            .containsExactly(
                activeClient3.toClientView(190)
            )
    }

    @Test
    fun `all clients from 01-01-2020 limit 2`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(2, `01-01-2020`))
            .containsExactly(
                activeClient3.toClientView(190),
                activeClient2.toClientView(181)
            )
    }

    @Test
    fun `all clients from 01-01-2020`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`))
            .containsExactly(
                activeClient3.toClientView(190),
                activeClient2.toClientView(181),
                activeClient1.toClientView(130)
            )
    }

    @Test
    fun `all clients from 04-01-2020`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusDays(3)))
            .containsExactly(
                activeClient3.toClientView(190),
                activeClient2.toClientView(181),
                activeClient1.toClientView(70)
            )
    }

    @Test
    fun `all clients from 05-01-2020`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusDays(4)))
            .containsExactly(
                activeClient3.toClientView(190),
                activeClient1.toClientView(70),
                activeClient2.toClientView(1)
            )
    }

    @Test
    fun `all clients from 08-01-2020`() {
        persistAll()

        assertThat(clientRepository.getActiveClientsWithMostPoints(10, `01-01-2020`.plusDays(7)))
            .containsExactly(
                activeClient1.toClientView(70),
                activeClient2.toClientView(1)
            )
    }

    private fun persistAll() {
        persistAllDataForClient1()
        persistAllDataForClient2()
        persistAllDataForClient3()
        persistBlockClient()
    }

    private fun persistAllDataForClient1() {
        clientRepository.persist(activeClient1)

        appointmentRepository.persist(appointmentA1C1)
        itemRepository.persist(service1A1C1)
        itemRepository.persist(service2A1C1)
        itemRepository.persist(purchase1A1C1)

        appointmentRepository.persist(appointmentA2C1)
        itemRepository.persist(purchase1A2C1)

        appointmentRepository.persist(appointmentA3C1)
        itemRepository.persist(service1A3C1)
    }

    private fun persistAllDataForClient2() {
        clientRepository.persist(activeClient2)

        appointmentRepository.persist(appointmentA1C2)

        appointmentRepository.persist(appointmentA2C2)
        itemRepository.persist(purchase1A2C2)

        appointmentRepository.persist(appointmentA3C2)
        itemRepository.persist(service1A3C2)
    }

    private fun persistAllDataForClient3() {
        clientRepository.persist(activeClient3)

        appointmentRepository.persist(appointmentA1C3)
        itemRepository.persist(service1A1C3)
    }

    private fun persistBlockClient() {
        clientRepository.persist(blockedClient)

        appointmentRepository.persist(appointmentA1CBlocked)
        itemRepository.persist(service1A1CBlocked)
    }

    fun Client.toClientView(points: Long) =
        ClientView(
            this.id,
            this.firstName,
            this.lastName,
            points
        )
}