package kr.hhplus.be.server.api.payment.request

data class PayRequest(
  val customerId: Long,
  val orderId: Long,
)
