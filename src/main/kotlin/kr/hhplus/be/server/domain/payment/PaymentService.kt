package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.payment.model.PaymentResult
import kr.hhplus.be.server.domain.payment.model.toResult
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentRepository: PaymentRepository,
) {
  fun get(id: Long): Payment =
    paymentRepository.findById(id)
      ?: throw NotFoundException(id, Payment::class.java)

  fun getResult(id: Long): PaymentResult = get(id).toResult()

  fun getByOrderId(orderId: Long): Payment? = paymentRepository.findByOrderId(orderId)

  fun create(order: Order): Payment =
    paymentRepository.save(
      Payment(
        order = order,
        price = order.totalPrice - order.discountPrice,
      ),
    )
}
