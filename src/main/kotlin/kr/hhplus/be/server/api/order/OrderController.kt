package kr.hhplus.be.server.api.order

import kr.hhplus.be.server.api.order.request.OrderCreateRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController {

  @PostMapping
  fun order(@RequestBody request: OrderCreateRequest) {
    TODO()

  }
}
