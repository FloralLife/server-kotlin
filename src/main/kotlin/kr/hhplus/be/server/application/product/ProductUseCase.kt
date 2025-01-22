package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.order.OrderProductRepository
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Service

@Service
class ProductUseCase(
  private val productService: ProductService,
  private val orderProductRepository: OrderProductRepository,
) {
  fun top5For3Days(): List<Product> {
    val top5ProductIds =
      orderProductRepository.findTop5MostPurchasedProductsInLast3Days()

    val products = productService.getAll(top5ProductIds)

    val productMap = products.associateBy { it.id }
    return top5ProductIds.mapNotNull { productMap[it] }
  }
}
