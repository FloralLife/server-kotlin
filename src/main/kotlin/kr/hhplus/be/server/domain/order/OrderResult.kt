package kr.hhplus.be.server.domain.order

import java.time.LocalDateTime

data class OrderResult(
  val id: Long,
  val address: String,
  val customerId: Long,
  val customerCouponId: Long?,
  val status: OrderStatus,
  val totalPrice: Int,
  val discountPrice: Int,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)

fun Order.toResult() = OrderResult(
  id = this.id,
  address = this.address,
  customerId = this.customer.id,
  customerCouponId = this.customerCoupon?.id,
  status = this.status,
  totalPrice = this.totalPrice,
  discountPrice = this.discountPrice,
  createdAt = this.createdAt,
  updatedAt = this.updatedAt,
)
