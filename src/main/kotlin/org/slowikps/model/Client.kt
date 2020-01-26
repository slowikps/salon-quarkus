package org.slowikps.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.NamedQuery

//id,first_name,last_name,email,phone,gender,banned
enum class Status {
    ACTIVE, BLOCKED
}

@NamedQuery(
    name = "Client.getMostLoyal",
    query = """
        SELECT NEW org.slowikps.model.ClientView(c.id, c.firstName, c.lastName, SUM(p.loyaltyPoints + s.loyaltyPoints))
        FROM Client c JOIN Appointment a ON c.id = a.clientId
            LEFT JOIN Purchase p ON a.id = p.appointmentId
            LEFT JOIN Service s ON a.id = s.appointmentId
        WHERE c.status = 'ACTIVE' AND a.startTime > :from
        GROUP BY c 
        ORDER BY SUM(p.loyaltyPoints + s.loyaltyPoints) DESC NULLS LAST
    """
)
@Entity
data class Client(
    @Id
    var id: UUID? = null,
    @Column(name = "first_name")
    var firstName: String? = null,
    @Column(name = "last_name")
    var lastName: String? = null,
    var email: String? = null, //TODO validation
    var phone: String? = null, //TODO validation
    var gender: String? = null,
    @Enumerated(EnumType.STRING)
    var status: Status? = null
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