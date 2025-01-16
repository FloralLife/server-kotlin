package kr.hhplus.be.server.infra.payment

import kr.hhplus.be.server.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPaymentRepository : JpaRepository<Payment, Long> {
  fun findByOrderId(orderId: Long): Payment?
}
