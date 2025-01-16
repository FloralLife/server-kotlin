package kr.hhplus.be.server.api.order.response

import kr.hhplus.be.server.domain.order.OrderStatus
import kr.hhplus.be.server.domain.order.model.OrderProductResult
import kr.hhplus.be.server.domain.order.model.OrderResult
import java.time.LocalDateTime

data class OrderResponse(
  val id: Long,
  val address: String,
  val customerId: Long,
  val customerCouponId: Long?,
  val status: OrderStatus,
  val products: List<OrderProductResponse>,
  val totalPrice: Int,
  val discountPrice: Int,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)

data class OrderProductResponse(
  val productId: Long,
  val amount: Int
)

fun OrderResult.toResponse() =
  OrderResponse(
    id,
    address,
    customerId,
    customerCouponId,
    status,
    products.map { it.toResponse() },
    totalPrice,
    discountPrice,
    createdAt,
    updatedAt,
  )

fun OrderProductResult.toResponse() =
  OrderProductResponse(
    productId,
    amount,
  )
