package kr.hhplus.be.server.domain.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface OrderRepository {
    fun findById(id: Long): Order?

    fun findByCustomerId(
        customerId: Long,
        pageable: Pageable,
    ): Page<Order>

    fun save(order: Order): Order
}
