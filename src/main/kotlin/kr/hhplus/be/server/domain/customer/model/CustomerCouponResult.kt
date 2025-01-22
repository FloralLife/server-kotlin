package kr.hhplus.be.server.domain.customer.model

import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class CustomerCouponResult(
  val id: Long,
  val customerId: Long,
  val couponId: Long,
  val expirationDate: LocalDate,
  val status: CustomerCouponStatus,
  val usedAt: LocalDateTime?,
)

fun CustomerCoupon.toResult() =
  CustomerCouponResult(
    id = this.id,
    customerId = this.customer.id,
    couponId = this.coupon.id,
    expirationDate = this.expirationDate,
    status = this.status,
    usedAt = this.usedAt,
  )
