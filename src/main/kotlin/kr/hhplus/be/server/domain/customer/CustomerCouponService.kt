package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CustomerCouponService(
  val customerCouponRepository: CustomerCouponRepository
) {
  fun get(id: Long): CustomerCouponResult {
    return customerCouponRepository.findById(id)?.toResult()
      ?: throw HhpNotFoundException(id, CustomerCoupon::class.java)
  }

  fun getAll(customerId: Long): List<CustomerCouponResult> {
    return customerCouponRepository.findAllByCustomerId(customerId).map { it.toResult() }
  }

  fun create(customer: Customer, coupon: Coupon): CustomerCouponResult {
    return customerCouponRepository.save(
      CustomerCoupon(
        customer = customer,
        coupon = coupon,
        expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
        usedAt = null
      )
    ).toResult()
  }
}
