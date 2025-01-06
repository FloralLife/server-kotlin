package kr.hhplus.be.server.api.customer

import kr.hhplus.be.server.api.customer.request.PointChargeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController {
  class Customer(
    val id: Long,
    var point: Int
  )

  val customer: Customer = Customer(1L, 50_000_000)

  @GetMapping("/{customerId}")
  fun get(@PathVariable customerId: Long): Customer {
    check(customerId > 0L) { "유저 id는 양수입니다." }
    require(customerId == 1L) { "존재하지 않습니다." }
    return customer
  }

  @GetMapping("/{customerId}/point")
  fun getPoint(@PathVariable customerId: Long): Int {
    check(customerId > 0L) { "유저 id는 양수입니다." }
    require(customerId == 1L) { "존재하지 않습니다." }
    return customer.point
  }

  @PutMapping("/{customerId}/point")
  fun chargePoint(@PathVariable customerId: Long, @RequestBody request: PointChargeRequest): Customer {
    check(customerId > 0L) { "유저 id는 양수입니다." }
    require(customerId == 1L) { "존재하지 않습니다." }
    check(customer.point < 1_000_000_000 - request.point) { "충전 가능 최대 금액은 1,000,000,000 원 입니다." }
    customer.point += request.point
    return customer
  }
}
