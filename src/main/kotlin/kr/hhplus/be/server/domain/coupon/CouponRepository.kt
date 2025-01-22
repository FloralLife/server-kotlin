package kr.hhplus.be.server.domain.coupon

interface CouponRepository {
  fun findById(id: Long): Coupon?

  fun save(coupon: Coupon): Coupon

  fun findForUpdateById(id: Long): Coupon?
}
