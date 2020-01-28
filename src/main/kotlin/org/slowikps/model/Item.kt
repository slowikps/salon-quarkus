package org.slowikps.model

import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

enum class Type {
    SERVICE, PURCHASE
}

@Entity
class Item( //TODO name is not great
    @Id
    var id: UUID,
    @Column(name = "appointment_id")
    var appointmentId: UUID,
    var name: String,
    var price: BigDecimal,
    var type: Type,
    @Column(name = "loyalty_points")
    var loyaltyPoints: Int
) {

    companion object Factory {
        fun fromString(line: String, type: Type): Item {
            val split: List<String> = line.split(",")

            if (split.size != 5) throw IllegalArgumentException("Malformed line: $line")

            return Item(
                UUID.fromString(split[0]),
                UUID.fromString(split[1]),
                split[2],
                BigDecimal(split[3]),
                type,
                split[4].toInt() //Is this correct type?
            )
        }
    }
}
