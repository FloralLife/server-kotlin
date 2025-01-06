package kr.hhplus.be.server.api.coupon.request

data class CouponCreateRequest(
  val name: String,
  val discountRate: Int,
  val stock: Int,
  val expirationPeriod: Int
)
