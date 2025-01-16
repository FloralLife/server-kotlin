package kr.hhplus.be.server.api.coupon

import kr.hhplus.be.server.api.coupon.request.CreateCouponRequest
import kr.hhplus.be.server.api.coupon.response.CouponResponse
import kr.hhplus.be.server.api.coupon.response.toResponse
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.command.toCommand
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupons")
class CouponController(
  val couponService: CouponService,
) {
  @GetMapping("/{couponId}")
  fun get(
    @PathVariable couponId: Long,
  ): CouponResponse = couponService.get(couponId).toResponse()

  @PostMapping
  fun create(
    @RequestBody request: CreateCouponRequest,
  ): CouponResponse = couponService.create(request.toCommand()).toResponse()
}
