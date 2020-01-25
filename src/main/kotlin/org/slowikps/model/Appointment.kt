package org.slowikps.model

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Appointment(
    @Id
    val id: UUID,
    @Column(name = "client_id")
    val clientId: UUID,
    @Column(name = "start_time")
    val startTime: OffsetDateTime,
    @Column(name = "end_time")
    val endTime: OffsetDateTime
) {

    companion object Factory {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        fun fromString(line: String): Appointment {
            val split: List<String> = line.split(",")

            if (split.size != 4) throw IllegalArgumentException("Malformed line: $line")

            return Appointment(
                UUID.fromString(split[0]),
                UUID.fromString(split[1]),
                OffsetDateTime.parse(split[2], dateFormat),
                OffsetDateTime.parse(split[3], dateFormat)
            )
        }
    }
}