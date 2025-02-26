package kr.hhplus.be.server.application.payment.event

import kr.hhplus.be.server.domain.payment.Payment

data class PaymentCompleteEvent(
  var eventId: Long = 0,
  val price: Int,
  val orderId: Long,
  val orderProducts: List<OrderProductInfo>
)

data class OrderProductInfo(
  val productId: Long,
  val amount: Int,
)

fun Payment.toEvent() = PaymentCompleteEvent(
  price = price,
  orderId = order.id,
  orderProducts = order.products.map { OrderProductInfo(it.product.id, it.amount) }
)

