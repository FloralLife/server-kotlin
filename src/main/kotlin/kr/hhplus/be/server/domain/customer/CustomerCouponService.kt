package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.model.CustomerCouponResult
import kr.hhplus.be.server.domain.customer.model.toResult
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CustomerCouponService(
  val customerCouponRepository: CustomerCouponRepository,
) {
  fun get(id: Long): CustomerCoupon =
    customerCouponRepository.findById(id)
      ?: throw NotFoundException(id, CustomerCoupon::class.java)

  fun getResult(id: Long): CustomerCouponResult = get(id).toResult()

  fun getAllResult(customerId: Long): List<CustomerCouponResult> =
    customerCouponRepository.findAllByCustomerId(customerId).map {
      it.toResult()
    }

  fun create(
    customer: Customer,
    coupon: Coupon,
  ): CustomerCouponResult =
    customerCouponRepository
      .save(
        CustomerCoupon(
          customer = customer,
          coupon = coupon,
          expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
          usedAt = null,
        ),
      ).toResult()
}
