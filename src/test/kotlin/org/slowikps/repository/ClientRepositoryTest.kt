package org.slowikps.repository

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.slowikps.config.DatabaseResource

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
class ClientRepositoryTest {

}