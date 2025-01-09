package kr.hhplus.be.server.domain.customer

interface CustomerCouponRepository {
  fun findById(id: Long): CustomerCoupon?

  fun findAllByCustomerId(customerId: Long): List<CustomerCoupon>

  fun save(customerCoupon: CustomerCoupon): CustomerCoupon
}
