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
    var id: UUID,
    @Column(name = "client_id")
    var clientId: UUID,
    @Column(name = "start_time")
    var startTime: OffsetDateTime,
    @Column(name = "end_time")
    var endTime: OffsetDateTime
) {

    companion object Factory {
        private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
        fun fromString(line: String): Appointment {
            val split: List<String> = line.split(",")

            if (split.size != 4) throw IllegalArgumentException("Malformed line: $line")

            val startTime = OffsetDateTime.parse(split[2], dateFormat)
            val endTime = OffsetDateTime.parse(split[3], dateFormat)
            if (endTime.isBefore(startTime)) throw IllegalArgumentException("Appointment start time [$startTime] is after end time [$endTime]")
            return Appointment(
                UUID.fromString(split[0]),
                UUID.fromString(split[1]),
                startTime,
                endTime
            )
        }
    }
}