package kr.hhplus.be.server.domain.customer

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "customer")
class Customer(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,
  var point: Int = 0,
) {
  companion object {
    const val MAX_POINT = 1_000_000_000
  }

  init {
    require(point >= 0) { "Point must not be negative" }
  }

  fun usePoint(amount: Int) {
    require(amount <= point) { "Cannot use more than balance. balance : $point" }
    point -= amount
  }

  fun chargePoint(amount: Int) {
    require(amount <= MAX_POINT - point) { "Cannot charge more than $MAX_POINT" }
    point += amount
  }
}
