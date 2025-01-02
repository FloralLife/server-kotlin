package kr.hhplus.be.server.api

import kr.hhplus.be.server.api.ProductController.Product
import kr.hhplus.be.server.api.UserController.User
import kr.hhplus.be.server.api.request.OrderCreateRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.random.Random

@RestController
@RequestMapping("/api/orders")
class OrderController {
  class Order(
    val id: Long,
    val status: String,
    val products: List<OrderProduct>,
    val userId: Long,
    val address: String,
    val totalPrice: Int,
    val userCouponId: Long?,
    val discountPrice: Int?,
  )

  class OrderProduct(
    val id: Long,
    val productId: Long,
    val orderId: Long,
    val amount: Int
  )

  val user: User = User(1L, 50_000_000)
  val product = Product(1L, "맥북", 50, 2_000_000, LocalDateTime.now())

  @PostMapping
  fun order(@RequestBody request: OrderCreateRequest): Order {
    check(request.userId > 0L) { "유저 id는 양수입니다." }
    require(request.userId == 1L) { "유저가 존재하지 않습니다." }

    if (request.userCouponId != null) {
      check(request.userCouponId > 0L) { "유저 쿠폰 id는 양수입니다." }
      require(request.userCouponId <= 2L) { "유저 쿠폰이 존재하지 않습니다." }
      check(request.userCouponId == 1L) { "만료된 쿠폰입니다." }
    }

    var totalPrice = 0;

    request.products.forEach {
      check(it.productId > 0L) { "상품 id는 양수입니다." }
      require(it.productId == 1L) { "상품이 존재하지 않습니다." }
      check(product.stock >= it.amount) { "상품의 재고가 부족합니다." }
      totalPrice += it.amount * product.price
    }

    var discountPrice = 0;
    if (request.userCouponId != null) {
      discountPrice = (totalPrice * 0.01).toInt()
      totalPrice = (totalPrice * 0.99).toInt()
    }

    check(totalPrice <= user.point) { "포인트가 부족합니다" }

    val products = request.products.map { OrderProduct(Random.nextLong(0L, 1_000_000L), it.productId, 1L, 5) }
    return Order(
      1L,
      "PRODUCT_PREPARING",
      products,
      request.userId,
      request.address,
      totalPrice,
      request.userCouponId,
      discountPrice
    )
  }
}
