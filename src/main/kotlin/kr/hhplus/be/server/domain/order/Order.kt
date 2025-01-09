package kr.hhplus.be.server.domain.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.BaseEntity
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import java.time.LocalDateTime

@Entity
@Table(name = "order")
class Order(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,

  var status: OrderStatus = OrderStatus.PAYMENT_WAITING,

  val address: String,

  var totalPrice: Int = 0,

  var paidAt: LocalDateTime? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  val customer: Customer,

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_coupon_id")
  val customerCoupon: CustomerCoupon? = null,

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "order")
  val products: MutableList<OrderProduct> = mutableListOf(),

  var discountPrice: Int
): BaseEntity() {

}
