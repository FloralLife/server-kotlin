package kr.hhplus.be.server.infra.order

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val jpaOrderRepository: JpaOrderRepository,
) : OrderRepository {
    override fun findById(id: Long): Order? = jpaOrderRepository.findByIdOrNull(id)

    override fun findByCustomerId(
        customerId: Long,
        pageable: Pageable,
    ): Page<Order> = jpaOrderRepository.findByCustomerId(customerId, pageable)

    override fun save(order: Order): Order = jpaOrderRepository.save(order)
}
