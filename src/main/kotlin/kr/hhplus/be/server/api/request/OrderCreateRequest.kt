package kr.hhplus.be.server.api.request

data class OrderCreateRequest(
  val address: String,
  val products: List<OrderProductRequest>,
  val userId: Long,
  val userCouponId: Long? = null
)

data class OrderProductRequest(
  val productId: Long,
  val amount: Int
)
