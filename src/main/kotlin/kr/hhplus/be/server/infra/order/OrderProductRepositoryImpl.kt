package kr.hhplus.be.server.infra.order

import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.order.OrderProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderProductRepositoryImpl(
  private val jpaOrderProductRepository: JpaOrderProductRepository,
) : OrderProductRepository {
  override fun findByOrderId(
    orderId: Long,
    pageable: Pageable,
  ): Page<OrderProduct> = jpaOrderProductRepository.findAllByOrderId(orderId, pageable)

  override fun saveAll(orderProducts: List<OrderProduct>): List<OrderProduct> =
    jpaOrderProductRepository.saveAll(orderProducts)

  override fun findTop5MostPurchasedProductsInLast3Days(): List<Long> =
    jpaOrderProductRepository.findTop5MostPurchasedProductsInLast3Days(
      startDate = LocalDateTime.now().minusDays(3),
    )
}
