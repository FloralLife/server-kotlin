package kr.hhplus.be.server.api.customer

import kr.hhplus.be.server.api.customer.request.CustomerCouponCreateRequest
import kr.hhplus.be.server.api.customer.response.CustomerCouponResponse
import kr.hhplus.be.server.api.customer.response.toResponse
import kr.hhplus.be.server.application.customer.CustomerUseCase
import kr.hhplus.be.server.domain.customer.CustomerCouponService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers/{customerId}/coupons")
class CustomerCouponController(
    private val customerUseCase: CustomerUseCase,
    private val customerCouponService: CustomerCouponService,
) {
    @GetMapping
    fun getAll(
        @PathVariable customerId: Long,
    ): List<CustomerCouponResponse> = customerCouponService.getAllResult(customerId).map { it.toResponse() }

    @GetMapping("/{customerCouponId}")
    fun get(
        @PathVariable customerId: Long,
        @PathVariable customerCouponId: Long,
    ): CustomerCouponResponse = customerCouponService.getResult(customerCouponId).toResponse()

    @PostMapping
    fun registerCoupon(
        @PathVariable customerId: Long,
        @RequestBody request: CustomerCouponCreateRequest,
    ): CustomerCouponResponse = customerUseCase.issueCoupon(customerId, request.couponId).toResponse()
}
