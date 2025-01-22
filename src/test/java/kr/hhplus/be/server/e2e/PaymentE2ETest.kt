package kr.hhplus.be.server.e2e

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.api.order.response.OrderResponse
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderProductRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.product.JpaProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class PaymentE2ETest @Autowired constructor(
  private val jpaCustomerRepository: JpaCustomerRepository,
  private val jpaOrderProductRepository: JpaOrderProductRepository,
  private val jpaProductRepository: JpaProductRepository,
  private val jpaOrderRepository: JpaOrderRepository,
  private val mockMvc: MockMvc,
) {
  data class OrderId(val id: Long = 0L)

  @BeforeEach
  fun setUp() {
    jpaOrderProductRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaProductRepository.deleteAll()
  }

  @Test
  @DisplayName("동시에 여러 결제 요청이 왔어도 유저의 포인트는 따로 계산")
  fun pay() {
    val product = jpaProductRepository.save(Product(0L, "A", 30, 1_000))
    val customer = jpaCustomerRepository.save(Customer(0L, 20_000))

    val list: List<String> = List(30) {
      mockMvc.post("/api/orders") {
        contentType = MediaType.APPLICATION_JSON
        content =
          "{ \"address\": \"zep\", \"customerId\": ${customer.id}, \"products\": [{ \"productId\": ${product.id}, \"amount\": 1 }]}"
        header("customerId", customer.id)
      }.andReturn().response.contentAsString
    }

    val objectMapper = ObjectMapper().apply {
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    val orderResponses: List<OrderId> =
      list.map { objectMapper.readValue(it, OrderId::class.java) }

    val results = orderResponses.parallelStream().map {
      mockMvc.post("/api/payments") {
        contentType = MediaType.APPLICATION_JSON
        content = "{ \"customerId\": ${customer.id}, \"orderId\": ${it.id}}  }"
        header("customerId", customer.id)
      }.andReturn().response.status
    }.toList()

    assertEquals(20, results.count { it == 200 })
    assertEquals(10, results.count { it == 400 })
  }
}
