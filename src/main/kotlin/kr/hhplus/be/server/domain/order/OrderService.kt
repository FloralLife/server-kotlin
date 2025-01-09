package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.application.order.command.CreateOrderCommand
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val orderProductRepository: OrderProductRepository

) {
  fun get(id: Long): Order {
    return orderRepository.findById(id) ?: throw HhpNotFoundException(id, Order::class.java)
  }

  fun create(command: CreateOrderCommand): Order {
    val order = Order.create(command)
    orderRepository.save(order)
    orderProductRepository.saveAll(order.products)
    return order
  }
}
