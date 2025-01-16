package kr.hhplus.be.server.api.customer.response

import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import kr.hhplus.be.server.domain.customer.model.CustomerCouponResult
import java.time.LocalDate
import java.time.LocalDateTime

data class CustomerCouponResponse(
  val id: Long,
  val customerId: Long,
  val couponId: Long,
  val expirationDate: LocalDate,
  val status: CustomerCouponStatus,
  val usedAt: LocalDateTime?,
)

fun CustomerCouponResult.toResponse() =
  CustomerCouponResponse(
    id = this.id,
    customerId = this.customerId,
    couponId = this.couponId,
    expirationDate = this.expirationDate,
    status = this.status,
    usedAt = this.usedAt,
  )
