package kr.hhplus.be.server.application.customer

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.customer.model.CustomerCouponResult
import kr.hhplus.be.server.domain.customer.CustomerCouponService
import kr.hhplus.be.server.domain.customer.CustomerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerUseCase(
  private val couponService: CouponService,
  private val customerService: CustomerService,
  private val customerCouponService: CustomerCouponService,
) {
  @Transactional
  fun issueCoupon(customerId: Long, couponId: Long): CustomerCouponResult {
    val customer = customerService.get(customerId)
    val coupon = couponService.getWithLock(couponId)
    coupon.issue()
    return customerCouponService.create(customer, coupon)
  }
}
