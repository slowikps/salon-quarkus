package org.slowikps.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.NamedQuery

enum class Status {
    ACTIVE, BLOCKED, DELETED
}

@NamedQuery(
    name = "Client.getMostLoyal",
    query = """
        SELECT NEW org.slowikps.model.ClientView(c.id, c.firstName, c.lastName, SUM(i.loyaltyPoints))
        FROM Client c JOIN Appointment a ON c.id = a.clientId JOIN Item i ON a.id = i.appointmentId
        WHERE c.status = 'ACTIVE' AND a.endTime > :from
        GROUP BY c 
        ORDER BY SUM(i.loyaltyPoints) DESC NULLS LAST
    """
)
@Entity
data class Client(
    @Id
    var id: UUID,
    @Column(name = "first_name")
    var firstName: String,
    @Column(name = "last_name")
    var lastName: String,
    var email: String, //TODO validation
    var phone: String, //TODO validation
    var gender: String,
    @Enumerated(EnumType.STRING)
    var status: Status
) {

    companion object Factory {
        fun fromString(line: String): Client {
            val split: List<String> = line.split(",")

            if (split.size != 7) throw IllegalArgumentException("Malformed line: $line")
            //TODO regex with capturing group
            return Client(
                UUID.fromString(split[0]),
                split[1],
                split[2],
                split[3],
                split[4],
                split[5],
                if (split[6] == "false") Status.ACTIVE
                else Status.BLOCKED
            )
        }
    }
}
@DefaultConstructor
data class ClientPut(
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var gender: String,
    var status: Status
) {
    fun toClient(id: UUID): Client =
        Client(
            id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
            gender = gender,
            status = status
        )
}