package kr.hhplus.be.server.api.order.request

data class OrderCreateRequest(
  val address: String,
  val products: List<OrderProductRequest>,
  val customerId: Long,
  val customerCouponId: Long? = null
)

data class OrderProductRequest(
  val productId: Long,
  val amount: Int
)
