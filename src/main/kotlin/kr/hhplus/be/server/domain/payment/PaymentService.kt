package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.model.PaymentResult
import kr.hhplus.be.server.domain.payment.model.toResult
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentRepository: PaymentRepository
) {
  fun get(id: Long): Payment {
    return paymentRepository.findById(id) ?: throw HhpNotFoundException(id, Payment::class.java)
  }

  fun getResult(id: Long): PaymentResult {
    return get(id).toResult()
  }

  fun getByOrderId(orderId: Long): Payment? {
    return paymentRepository.findByOrderId(orderId)
  }

  fun create(order: Order): Payment {
    return paymentRepository.save(
      Payment(
        order = order,
        price = order.totalPrice - order.discountPrice
      )
    )
  }
}
