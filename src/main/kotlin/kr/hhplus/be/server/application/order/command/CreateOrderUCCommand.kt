package kr.hhplus.be.server.application.order.command

import kr.hhplus.be.server.api.order.request.CreateOrderProductRequest
import kr.hhplus.be.server.api.order.request.CreateOrderRequest

data class CreateOrderUCCommand(
  val address: String,
  val customerId: Long,
  val customerCouponId: Long?,
  val products: List<CreateOrderProductUCCommand>,
) {
  init {
    val productIds = products.map { it.productId }
    require(productIds.size == productIds.distinct().size) { "잘못된 요청 형식입니다." }
  }
}

data class CreateOrderProductUCCommand(
  val productId: Long,
  val amount: Int,
)

fun CreateOrderRequest.toCommand() =
  CreateOrderUCCommand(
    address,
    customerId,
    customerCouponId,
    products.map { it.toCommand() },
  )

fun CreateOrderProductRequest.toCommand() =
  CreateOrderProductUCCommand(
    productId,
    amount,
  )
