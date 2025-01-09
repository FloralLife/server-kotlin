package kr.hhplus.be.server.domain.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface OrderProductRepository {
  fun findByOrderId(orderId: Long, pageable: Pageable): Page<OrderProduct>

  fun saveAll(orderProducts: List<OrderProduct>): List<OrderProduct>

  fun findTop5MostPurchasedProductsInLast3Days(): List<Long>
}
