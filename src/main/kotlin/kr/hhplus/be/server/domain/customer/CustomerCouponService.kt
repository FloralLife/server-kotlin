package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CustomerCouponService(
  val customerCouponRepository: CustomerCouponRepository
) {
  fun get(id: Long): CustomerCoupon {
    return customerCouponRepository.findById(id) ?: throw HhpNotFoundException(id, CustomerCoupon::class.java)
  }

  fun create(customer: Customer, coupon: Coupon): CustomerCoupon {
    return customerCouponRepository.save(
      CustomerCoupon(
        customer = customer,
        coupon = coupon,
        expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
        usedAt = null
      )
    )
  }
}
