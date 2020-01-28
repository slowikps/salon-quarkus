package org.slowikps.model

import java.util.UUID

data class ClientView(
    var id: UUID? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var loyaltyPoints: Long = 0
)