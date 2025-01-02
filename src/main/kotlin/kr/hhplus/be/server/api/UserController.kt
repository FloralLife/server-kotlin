package kr.hhplus.be.server.api

import kr.hhplus.be.server.api.request.PointChargeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {
  class User(
    val id: Long,
    var point: Int
  )

  val user: User = User(1L, 50_000_000)

  @GetMapping("/{userId}")
  fun get(@PathVariable userId: Long): User {
    require(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    return user
  }

  @GetMapping("/{userId}/point")
  fun getPoint(@PathVariable userId: Long): Int {
    require(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    return user.point
  }

  @PutMapping("/{userId}/point")
  fun chargePoint(@PathVariable userId: Long, @RequestBody request: PointChargeRequest): User {
    require(userId > 0L) { "유저 id는 양수입니다." }
    require(userId == 1L) { "존재하지 않습니다." }
    user.point += request.point
    return user
  }
}
