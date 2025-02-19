package kr.hhplus.be.server.infra.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
  private val jpaCouponRepository: JpaCouponRepository,
) : CouponRepository {
  override fun findById(id: Long): Coupon? = jpaCouponRepository.findByIdOrNull(id)

  override fun save(coupon: Coupon): Coupon = jpaCouponRepository.save(coupon)

  override fun findForUpdateById(id: Long): Coupon? = jpaCouponRepository.findForUpdateById(id)
}
