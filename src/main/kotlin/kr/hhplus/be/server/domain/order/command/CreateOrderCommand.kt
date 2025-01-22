package kr.hhplus.be.server.domain.order.command

import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.product.Product

data class CreateOrderCommand(
  val address: String,
  val customer: Customer,
  val customerCoupon: CustomerCoupon?,
  val products: List<CreateOrderProductCommand>,
  val discountRate: Int,
)

data class CreateOrderProductCommand(
  val product: Product,
  val amount: Int,
)
