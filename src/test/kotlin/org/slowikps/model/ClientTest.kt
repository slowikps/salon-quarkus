package org.slowikps.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class ClientTest {

    @Test
    fun `correct line should be parsed successful`() {
        Assertions.assertEquals(
            Client(
                UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"),
                "Dori",
                "Dietrich",
                "patrica@keeling.net",
                "(272) 301-6356",
                "Male",
                Status.ACTIVE
            ),
            Client.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false")
        )
    }

    @Test
    fun `when invalid UUID provided exception should be thrown`() {
        val exception = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Client.fromString("NOT_UUID,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false")
        }
        Assertions.assertEquals(
            "Invalid UUID string: NOT_UUID",
            exception.message
        )
    }

    @Test
    fun `when line contains too many fields exception should be thrown`() {
        val exception = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Client.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false, extra")
        }
        Assertions.assertEquals(
            "Malformed line: e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false, extra",
            exception.message
        )
    }

    @Test
    fun `when line contains too few fields exception should be thrown`() {
        val exception = Assertions.assertThrows(IllegalArgumentException::class.java) {
            Client.fromString("JUST, TWO")
        }
        Assertions.assertEquals(
            "Malformed line: JUST, TWO",
            exception.message
        )
    }
}