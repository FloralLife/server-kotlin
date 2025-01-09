package kr.hhplus.be.server.domain.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductTest {
    @Test
    @DisplayName("재고보다 많은 양을 구매하려고 하면 IllegalStateException")
    fun purchaseThenIllegalStateException() {
        val product = Product(1L, "A", 3, 1000)

        assertThrows(IllegalStateException::class.java) { product.purchase(4) }
    }

    @Test
    @DisplayName("재고 이하의 양 구매시 성공")
    fun purChase() {
        val product = Product(1L, "A", 3, 1000)

        product.purchase(2)

        assertEquals(1, product.stock)
    }
}
