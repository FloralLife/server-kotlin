package kr.hhplus.be.server.api.order

import kr.hhplus.be.server.api.order.request.CreateOrderRequest
import kr.hhplus.be.server.api.order.response.OrderResponse
import kr.hhplus.be.server.api.order.response.toResponse
import kr.hhplus.be.server.application.order.OrderUseCase
import kr.hhplus.be.server.application.order.command.toCommand
import kr.hhplus.be.server.domain.order.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
  private val orderUseCase: OrderUseCase,
  private val orderService: OrderService,
) {
  @GetMapping("/{orderId}")
  fun get(
    @PathVariable orderId: Long,
  ): OrderResponse = orderService.getResult(orderId).toResponse()

  @PostMapping
  fun order(
    @RequestBody request: CreateOrderRequest,
  ): OrderResponse = orderUseCase.order(request.toCommand()).toResponse()
}
