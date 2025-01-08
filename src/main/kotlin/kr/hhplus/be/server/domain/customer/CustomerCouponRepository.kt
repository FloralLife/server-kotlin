package kr.hhplus.be.server.domain.customer

interface CustomerCouponRepository {
  fun findById(id: Long): CustomerCoupon?

  fun save(customerCoupon: CustomerCoupon): CustomerCoupon
}
