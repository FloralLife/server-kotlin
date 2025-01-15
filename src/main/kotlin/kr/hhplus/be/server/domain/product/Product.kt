package kr.hhplus.be.server.domain.product

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.BaseEntity

@Entity
@Table(name = "product")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    var stock: Int,
    var price: Int,
) : BaseEntity() {
    init {
        require(stock >= 0) { "Stock must not be negative." }
        require(price >= 0) { "Price must not be negative." }
    }

    fun purchase(amount: Int) {
        check(amount <= stock) { "Cannot purchase more than stock. stock : $stock" }
        stock -= amount
    }
}
