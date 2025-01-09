package kr.hhplus.be.server.api.order.request

data class CreateOrderRequest(
  val address: String,
  val products: List<CreateOrderProductRequest>,
  val customerId: Long,
  val customerCouponId: Long? = null
)

data class CreateOrderProductRequest(
  val productId: Long,
  val amount: Int
)
