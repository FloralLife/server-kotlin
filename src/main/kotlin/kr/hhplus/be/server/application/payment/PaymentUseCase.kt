package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.event.toEvent
import kr.hhplus.be.server.domain.customer.CustomerService
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.payment.PaymentService
import kr.hhplus.be.server.domain.payment.model.PaymentResult
import kr.hhplus.be.server.domain.payment.model.toResult
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentUseCase(
  private val customerService: CustomerService,
  private val paymentService: PaymentService,
  private val orderService: OrderService,
  private val eventPublisher: ApplicationEventPublisher
) {
  @Transactional
  fun pay(
    customerId: Long,
    orderId: Long,
  ): PaymentResult {
    val customer = customerService.getWithLock(customerId)
    val order = orderService.get(orderId)

    val payment = paymentService.create(order)

    customer.usePoint(payment.price)
    order.pay()

    eventPublisher.publishEvent(payment.toEvent())
    return payment.toResult()
  }
}
