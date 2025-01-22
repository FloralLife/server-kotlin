package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {
    @Mock
    lateinit var productRepository: ProductRepository

    @InjectMocks
    lateinit var productService: ProductService

    @Test
    @DisplayName("존재하지 않는 상품 조회시 NotFoundException 발생")
    fun getThenNotFound() {
        whenever(productRepository.findById(anyLong())).thenReturn(null)

        assertThrows<NotFoundException> { productService.get(randomId()) }
    }
}
