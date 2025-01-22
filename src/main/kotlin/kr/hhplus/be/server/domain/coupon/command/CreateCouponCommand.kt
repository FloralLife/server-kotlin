package kr.hhplus.be.server.domain.coupon.command

import kr.hhplus.be.server.api.coupon.request.CreateCouponRequest

data class CreateCouponCommand(
  val name: String,
  val discountRate: Int,
  val stock: Int,
  val expirationPeriod: Int,
)

fun CreateCouponRequest.toCommand() =
  CreateCouponCommand(
    name,
    discountRate,
    stock,
    expirationPeriod,
  )
