package kr.hhplus.be.server.domain.order.model

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.order.OrderStatus
import java.time.LocalDateTime

data class OrderResult(
  val id: Long,
  val address: String,
  val customerId: Long,
  val customerCouponId: Long?,
  val status: OrderStatus,
  val products: List<OrderProductResult>,
  val totalPrice: Int,
  val discountPrice: Int,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)

data class OrderProductResult(
  val productId: Long,
  val amount: Int
)

fun Order.toResult() =
  OrderResult(
    id = this.id,
    address = this.address,
    customerId = this.customer.id,
    customerCouponId = this.customerCoupon?.id,
    status = this.status,
    products = this.products.map { it.toResult() },
    totalPrice = this.totalPrice,
    discountPrice = this.discountPrice,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
  )

fun OrderProduct.toResult() =
  OrderProductResult(
    productId = this.product.id,
    amount = this.amount,
  )
