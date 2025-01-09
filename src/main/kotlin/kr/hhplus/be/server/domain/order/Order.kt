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
import kr.hhplus.be.server.application.order.command.CreateOrderCommand
import kr.hhplus.be.server.application.order.command.CreateOrderProductCommand
import kr.hhplus.be.server.domain.BaseEntity
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.payment.Payment

@Entity
@Table(name = "order")
class Order(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,

  var status: OrderStatus = OrderStatus.PAYMENT_WAITING,

  var address: String,

  var totalPrice: Int = 0,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  val customer: Customer,

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_coupon_id")
  val customerCoupon: CustomerCoupon? = null,

  @OneToOne
  @JoinColumn(name = "payment_id")
  var payment: Payment? = null,

  @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "order")
  var products: List<OrderProduct> = listOf(),

  var discountPrice: Int = 0
) : BaseEntity() {
  companion object {
    fun create(command: CreateOrderCommand): Order {
//      return Order(
//        address = command.address,
//        totalPrice = command.totalPrice,
//        customer = command.customer,
//        customerCoupon = command.customerCoupon,
//        discountPrice = command.totalPrice * (command.discountRate / 100)
//      )
      val totalPrice = command.products
        .fold(0) { acc, productCommand -> acc + productCommand.product.price * productCommand.amount }
      val order = Order(
        address = command.address,
        totalPrice = totalPrice,
        customer = command.customer,
        customerCoupon = command.customerCoupon,
        discountPrice = totalPrice * command.discountRate / 100
      )
      val orderProducts = command.products.map {
        OrderProduct(
          product = it.product,
          order = order,
          amount = it.amount
        )
      }
      order.products = orderProducts
      return order
    }
  }

  fun cancel() {
    status = OrderStatus.CANCELLED
  }

  fun pay(payment: Payment) {
    status = OrderStatus.PAYMENT_COMPLETED
    this.payment = payment
  }

  fun createOrderProducts(command: List<CreateOrderProductCommand>): List<OrderProduct> {
    val orderProducts = command.map { OrderProduct(product = it.product, order = this, amount = it.amount) }
    products = orderProducts
    return orderProducts
  }
}
