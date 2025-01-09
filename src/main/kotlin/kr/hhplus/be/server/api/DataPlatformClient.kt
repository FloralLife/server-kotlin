package kr.hhplus.be.server.api

import kr.hhplus.be.server.domain.order.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DataPlatformClient {
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  fun sendOrder(order: Order) {
    Thread.sleep(5000)
    logger.info("Succeed to send order to data platform. id: ${order.id}")
  }
}
