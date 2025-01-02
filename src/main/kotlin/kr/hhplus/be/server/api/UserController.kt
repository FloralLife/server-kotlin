package kr.hhplus.be.server.api

import kr.hhplus.be.server.api.request.PointChargeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/users")
class UserController {
  class User(
    val id: Long,
    var point: Int
  )

  class UserCoupon(
    val id: Long,
    val couponId: Long,
    val expirationDate: LocalDate,
    val userId: Long,
    var orderId: Long?
  )

  val user: User = User(1L, 50_000_000)
  val userCoupon = UserCoupon(1L, 1L, LocalDate.MAX, 1L, null)

  @GetMapping("/{userId}")
  fun get(@PathVariable userId: Long): User {
    check(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    return user
  }

  @PostMapping("/{userId}/coupons/{couponId}")
  fun registerCoupon(@PathVariable userId: Long, @PathVariable couponId: Long): UserCoupon {
    check(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "유저가 존재하지 않습니다." }

    check(couponId > 0L) { "쿠폰 id는 양수입니다." }
    require(couponId <= 2L) { "쿠폰이 존재하지 않습니다." }
    check(couponId == 1L) { "쿠폰의 재고가 없습니다." }

    return userCoupon
  }

  @GetMapping("/{userId}/point")
  fun getPoint(@PathVariable userId: Long): Int {
    check(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    return user.point
  }

  @PutMapping("/{userId}/point")
  fun chargePoint(@PathVariable userId: Long, @RequestBody request: PointChargeRequest): User {
    check(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    check(user.point < 1_000_000_000 - request.point) { "충전 가능 최대 금액은 1,000,000,000 원 입니다." }
    user.point += request.point
    return user
  }
}
