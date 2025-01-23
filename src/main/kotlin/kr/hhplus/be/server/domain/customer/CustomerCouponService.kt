package kr.hhplus.be.server.domain.customer

import jakarta.persistence.OptimisticLockException
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.model.CustomerCouponResult
import kr.hhplus.be.server.domain.customer.model.toResult
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.orm.ObjectOptimisticLockingFailureException
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
      .saveAndFlush(
        CustomerCoupon(
          customer = customer,
          coupon = coupon,
          expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
          usedAt = null,
        ),
      ).toResult()

  fun use(customerCoupon: CustomerCoupon): CustomerCoupon {
    try {
      customerCoupon.use()
      return customerCouponRepository.saveAndFlush(customerCoupon)
    } catch (ex: ObjectOptimisticLockingFailureException) {
      println("낙관적 락으로 인한 실패")
      throw IllegalStateException("사용할 수 없는 유저 쿠폰 입니다.")
    }
  }
}
