package kr.hhplus.be.server.api.customer

import kr.hhplus.be.server.api.customer.request.CustomerCouponCreateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/customers/{customerId}/coupons")
class CustomerCouponController {
  class CustomerCoupon(
    val id: Long,
    val couponId: Long,
    val expirationDate: LocalDate,
    val customerId: Long,
    var orderId: Long?
  )

  val customerCoupon = CustomerCoupon(1L, 1L, LocalDate.MAX, 1L, null)

  @GetMapping
  fun getAll(@PathVariable customerId: Long): List<CustomerCoupon> {
    return listOf()
  }

  @GetMapping("/{customerCouponId}")
  fun get(@PathVariable customerId: Long, @PathVariable customerCouponId: Long): CustomerCoupon {
    return customerCoupon
  }

  @PostMapping("/coupons")
  fun registerCoupon(@PathVariable customerId: Long, @RequestBody request: CustomerCouponCreateRequest): CustomerCoupon {
    check(customerId > 0L) { "유저 id는 양수입니다." }
    require(customerId == 1L) { "유저가 존재하지 않습니다." }

    val couponId = request.couponId

    check(couponId > 0L) { "쿠폰 id는 양수입니다." }
    require(couponId <= 2L) { "쿠폰이 존재하지 않습니다." }
    check(couponId == 1L) { "쿠폰의 재고가 없습니다." }

    return customerCoupon
  }
}
