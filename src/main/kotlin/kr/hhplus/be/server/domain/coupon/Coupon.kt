package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "coupon")
class Coupon(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,
  val name: String,
  val discountRate: Int,
  val stock: Int,
  val expirationPeriod: Int
) {


}
