package kr.hhplus.be.server.api.customer

import kr.hhplus.be.server.api.customer.request.PointChargeRequest
import kr.hhplus.be.server.api.customer.response.CustomerResponse
import kr.hhplus.be.server.api.customer.response.toResponse
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
  private val customerService: CustomerService,
) {
  @GetMapping("/{customerId}")
  fun get(
    @PathVariable customerId: Long,
  ): CustomerResponse = customerService.get(customerId).toResponse()

  @GetMapping("/{customerId}/point")
  fun getPoint(
    @PathVariable customerId: Long,
  ): Int = customerService.get(customerId).point

  @PostMapping
  fun create(): CustomerResponse = customerService.create().toResponse()

  @PutMapping("/{customerId}/point")
  fun chargePoint(
    @PathVariable customerId: Long,
    @RequestBody request: PointChargeRequest,
  ): Customer = customerService.chargePoint(customerId, request.amount)
}
