package org.slowikps.model

import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
@Entity
class Purchase(
    @Id
    val id: UUID,
    @Column(name = "appointment_id")
    val appointmentId: UUID,
    val name: String,
    val price: BigDecimal,
    @Column(name = "loyalty_points")
    val loyaltyPoints: Int
) {

    companion object Factory {
        fun fromString(line: String): Purchase {
            val split: List<String> = line.split(",")

            if (split.size != 5) throw IllegalArgumentException("Malformed line: $line")

            return Purchase(
                UUID.fromString(split[0]),
                UUID.fromString(split[1]),
                split[2],
                BigDecimal(split[3]),
                split[4].toInt() //Is this correct type?
            )
        }
    }
}
