package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.order.command.CreateOrderCommand
import kr.hhplus.be.server.domain.order.model.OrderResult
import kr.hhplus.be.server.domain.order.model.toResult
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val orderProductRepository: OrderProductRepository,
) {
  fun get(id: Long): Order =
    orderRepository.findById(id)
      ?: throw NotFoundException(id, Order::class.java)

  fun getResult(id: Long): OrderResult = get(id).toResult()

  fun create(command: CreateOrderCommand): Order {
    val order = Order.create(command)
    orderRepository.save(order)
    orderProductRepository.saveAll(order.products)
    return order
  }
}
