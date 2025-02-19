package kr.hhplus.be.server.domain.customer

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import kr.hhplus.be.server.domain.coupon.Coupon
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
  name = "customer_coupon",
  indexes = [Index(name = "customer_id_coupon_id", columnList = "customer_id, coupon_id", unique = true)],
)
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
  @Version
  var version: Long = 0L
) {
  fun isExpired(): Boolean = expirationDate.isBefore(LocalDate.now())

  fun use() {
    require(status == CustomerCouponStatus.UNUSED) { "이미 사용된 쿠폰입니다." }
    require(!isExpired()) { "만료된 쿠폰입니다." }

    status = CustomerCouponStatus.USED
    usedAt = LocalDateTime.now()
  }
}
