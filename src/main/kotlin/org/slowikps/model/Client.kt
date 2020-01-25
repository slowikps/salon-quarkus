package org.slowikps.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

//id,first_name,last_name,email,phone,gender,banned
enum class Status {
    ACTIVE, BLOCKED
}

// @NamedQuery(name = "Fruits.findAll",
//     query = "SELECT f FROM Fruit f ORDER BY f.name",
//     hints = @QueryHint(name = "org.hibernate.cacheable", value = "true") )
@Entity
data class Client(
    @Id
    val id: UUID,
    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "last_name")
    val lastName: String,
    val email: String, //TODO validation
    val phone: String, //TODO validation
    val gender: String,
    val status: Status
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