package kr.hhplus.be.server.domain.payment.model

import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentStatus
import kr.hhplus.be.server.domain.payment.PaymentType

data class PaymentResult(
  val id: Long,
  val orderId: Long,
  val status: PaymentStatus,
  val type: PaymentType,
  val price: Int,
)

fun Payment.toResult() = PaymentResult(id, order.id, status, type, price)
