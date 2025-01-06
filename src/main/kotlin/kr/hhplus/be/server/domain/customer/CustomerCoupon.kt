package kr.hhplus.be.server.domain.customer

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.coupon.Coupon
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "customer_coupon")
class CustomerCoupon(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  val customer: Customer,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false)
  val coupon: Coupon,

  val expirationDate: LocalDate,

  var status: CustomerCouponStatus = CustomerCouponStatus.UNUSED,

  var usedAt: LocalDateTime?,
) {
}
