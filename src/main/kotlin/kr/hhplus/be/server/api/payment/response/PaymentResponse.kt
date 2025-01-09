package kr.hhplus.be.server.api.payment.response

import kr.hhplus.be.server.domain.payment.PaymentStatus
import kr.hhplus.be.server.domain.payment.PaymentType
import kr.hhplus.be.server.domain.payment.model.PaymentResult

data class PaymentResponse(
  val id: Long,
  val orderId: Long,
  val status: PaymentStatus,
  val type: PaymentType,
  val price: Int,
)

fun PaymentResult.toResponse() = PaymentResponse(id, orderId, status, type, price)
