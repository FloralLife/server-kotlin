package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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

    assertThrows(NotFoundException::class.java) { productService.get(randomId()) }
  }

  @Test
  @DisplayName("여러 ID 조회시 존재하지 않는 id가 있는 경우 Not Found")
  fun getAllThenNotFound() {
    val product = Product(randomId(), "상품", 5, 1000)
    val ids = listOf(product.id, randomId())

    whenever(productRepository.findAllForUpdateByIds(ids))
      .thenReturn(listOf(product))
    whenever(productRepository.findAllByIds(ids))
      .thenReturn(listOf(product))

    assertThrows(NotFoundException::class.java) { productService.getAllWithLock(ids) }
    assertThrows(NotFoundException::class.java) { productService.getAll(ids) }
  }
}
