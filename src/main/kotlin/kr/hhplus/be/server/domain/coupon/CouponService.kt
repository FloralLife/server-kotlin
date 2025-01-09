package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.command.CreateCouponCommand
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service

@Service
class CouponService(
  private val couponRepository: CouponRepository
) {
  fun get(id: Long): Coupon {
    return couponRepository.findById(id) ?: throw HhpNotFoundException(id, Coupon::class.java)
  }

  fun create(command: CreateCouponCommand): Coupon {
    return couponRepository.save(
      Coupon(
        name = command.name,
        stock = command.stock,
        discountRate = command.discountRate,
        expirationPeriod = command.expirationPeriod,
      )
    )
  }

  fun getWithLock(id: Long): Coupon {
    return couponRepository.findForUpdateById(id) ?: throw HhpNotFoundException(id, Coupon::class.java)
  }
}
