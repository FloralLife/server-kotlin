package kr.hhplus.be.server.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupons")
class CouponController {
  class Coupon (
    val id: Long,
    val name: String,
    val discountRate: Int,
    var stock: Int,
    val expirationPeriod: Int
  )

  val coupon = Coupon(1L, "선착순 쿠폰", 1, 100, 90)
}
