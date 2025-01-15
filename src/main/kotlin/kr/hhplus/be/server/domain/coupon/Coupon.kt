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
    var stock: Int,
    val expirationPeriod: Int,
) {
    init {
        require(discountRate in 1..100) { "Discount rate must be between 1 and 100" }
        require(stock >= 0) { "Stock must not be negative" }
        require(expirationPeriod in 1..365) { "Expiration period must be between 1 and 365" }
    }

    fun issue() {
        require(stock > 0) { "All coupons have been exhausted" }
        stock--
    }
}
