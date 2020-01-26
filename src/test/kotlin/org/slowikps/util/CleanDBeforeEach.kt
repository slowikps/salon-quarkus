package org.slowikps.util

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import javax.inject.Inject

open class CleanDBeforeEach {
    @Inject
    private lateinit var flyway: Flyway

    @BeforeEach
    fun cleanDb() {
        flyway.clean()
        flyway.migrate()
    }
}