package kr.hhplus.be.server.infra.order

import kr.hhplus.be.server.domain.order.OrderProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrderProductRepository : JpaRepository<OrderProduct, Long> {
  fun findAllByOrderId(orderId: Long, pageable: Pageable): Page<OrderProduct>
}
