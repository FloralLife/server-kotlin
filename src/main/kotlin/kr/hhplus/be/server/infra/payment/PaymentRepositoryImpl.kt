package kr.hhplus.be.server.infra.payment

import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
  private val jpaPaymentRepository: JpaPaymentRepository
): PaymentRepository {
  override fun findById(id: Long): Payment? {
    return jpaPaymentRepository.findByIdOrNull(id)
  }

  override fun findByOrderId(orderId: Long): Payment? {
    return jpaPaymentRepository.findByOrderId(orderId)
  }

  override fun save(payment: Payment): Payment {
    return jpaPaymentRepository.save(payment)
  }
}
