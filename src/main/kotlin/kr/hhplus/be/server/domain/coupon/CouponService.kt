package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.coupon.command.CreateCouponCommand
import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
) {
    fun get(id: Long): Coupon =
        couponRepository.findById(id)
            ?: throw NotFoundException(id, Coupon::class.java)

    fun create(command: CreateCouponCommand): Coupon =
        couponRepository.save(
            Coupon(
                name = command.name,
                stock = command.stock,
                discountRate = command.discountRate,
                expirationPeriod = command.expirationPeriod,
            ),
        )

    fun getWithLock(id: Long): Coupon =
        couponRepository.findForUpdateById(id)
            ?: throw NotFoundException(id, Coupon::class.java)
}
