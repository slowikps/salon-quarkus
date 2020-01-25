package org.slowikps.config

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer

//https://github.com/testcontainers/testcontainers-java/issues/318
class KPostgreSQLContainer(imageName: String) : PostgreSQLContainer<KPostgreSQLContainer>(imageName)

class DatabaseResource : QuarkusTestResourceLifecycleManager {

    private val testDB: PostgreSQLContainer<*> = KPostgreSQLContainer("postgres:12.1")
        .withUsername("test")
        .withPassword("test_pass")
        .withExposedPorts(5432)

    override fun start(): MutableMap<String, String> {
        println("Starting test DB")
        testDB.start()
        return mutableMapOf(
            "quarkus.datasource.url" to testDB.getJdbcUrl(),
            "quarkus.datasource.username" to testDB.getUsername(),
            "quarkus.datasource.password" to testDB.getPassword(),
            "quarkus.hibernate-orm.dia1§2-==2-lect" to "org.hibernate.dialect.PostgreSQL10Dialect"
        )
    }

    override fun stop() {
        testDB.stop()
    }
}