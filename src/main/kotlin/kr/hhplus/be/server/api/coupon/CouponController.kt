package kr.hhplus.be.server.api.coupon

import kr.hhplus.be.server.api.coupon.request.CouponCreateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupons")
class CouponController {
  class Coupon(
    val id: Long,
    val name: String,
    val discountRate: Int,
    var stock: Int,
    val expirationPeriod: Int
  )

  val coupon = Coupon(1L, "선착순 쿠폰", 1, 100, 90)

  @GetMapping
  fun getAll(): List<Coupon> {
    return listOf(coupon)
  }

  @GetMapping("/{couponId}")
  fun get(@PathVariable couponId: String): Coupon {
    return coupon
  }

  @PostMapping
  fun add(@RequestBody request: CouponCreateRequest): Coupon {
    return coupon
  }
}
