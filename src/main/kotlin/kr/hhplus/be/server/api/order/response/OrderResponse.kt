package kr.hhplus.be.server.api.order.response

import kr.hhplus.be.server.domain.order.model.OrderResult
import kr.hhplus.be.server.domain.order.OrderStatus
import java.time.LocalDateTime

data class OrderResponse(
  val id: Long,
  val address: String,
  val customerId: Long,
  val customerCouponId: Long?,
  val status: OrderStatus,
  val totalPrice: Int,
  val discountPrice: Int,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)

fun OrderResult.toResponse() = OrderResponse(
  id, address, customerId, customerCouponId, status, totalPrice, discountPrice, createdAt, updatedAt
)

