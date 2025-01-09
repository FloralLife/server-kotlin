package kr.hhplus.be.server.api.payment

import kr.hhplus.be.server.api.payment.request.PayRequest
import kr.hhplus.be.server.api.payment.response.PaymentResponse
import kr.hhplus.be.server.api.payment.response.toResponse
import kr.hhplus.be.server.application.payment.PaymentUseCase
import kr.hhplus.be.server.domain.payment.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PaymentController(
  private val paymentService: PaymentService,
  private val paymentUseCase: PaymentUseCase
) {
  @GetMapping("/{paymentId}")
  fun get(@PathVariable paymentId: Long): PaymentResponse {
    return paymentService.getResult(paymentId).toResponse()
  }

  @PostMapping
  fun purchase(@RequestBody request: PayRequest): PaymentResponse {
    return paymentUseCase.pay(request.customerId, request.orderId).toResponse()
  }
}
