package kr.hhplus.be.server.api.coupon.response

import kr.hhplus.be.server.domain.coupon.Coupon

data class CouponResponse(
  val id: Long,
  val name: String,
  val discountRate: Int,
  val stock: Int,
  val expirationPeriod: Int
)

fun Coupon.toResponse() = CouponResponse(
  id = this.id,
  name = this.name,
  discountRate = this.discountRate,
  stock = this.stock,
  expirationPeriod = this.expirationPeriod
)
