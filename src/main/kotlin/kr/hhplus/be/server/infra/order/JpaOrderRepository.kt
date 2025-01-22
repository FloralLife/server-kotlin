package kr.hhplus.be.server.infra.order

import kr.hhplus.be.server.domain.order.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface JpaOrderRepository : JpaRepository<Order, Long> {
  fun findByCustomerId(
    customerId: Long,
    parable: Pageable,
  ): Page<Order>
}
