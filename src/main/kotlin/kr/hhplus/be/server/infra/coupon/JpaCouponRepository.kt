package kr.hhplus.be.server.infra.coupon

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.coupon.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface JpaCouponRepository : JpaRepository<Coupon, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  fun findForUpdateById(id: Long): Coupon?
}
