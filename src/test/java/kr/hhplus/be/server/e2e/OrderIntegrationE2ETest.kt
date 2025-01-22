package kr.hhplus.be.server.e2e

import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderProductRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.product.JpaProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationE2ETest @Autowired constructor(
  val jpaCustomerRepository: JpaCustomerRepository,
  val jpaOrderProductRepository: JpaOrderProductRepository,
  val jpaCustomerCouponRepository: JpaCustomerCouponRepository,
  var jpaCouponRepository: JpaCouponRepository,
  val jpaProductRepository: JpaProductRepository,
  val jpaOrderRepository: JpaOrderRepository,
  val mockMvc: MockMvc,
) {
  @BeforeEach
  fun setUp() {
    jpaOrderProductRepository.deleteAll()
    jpaCustomerCouponRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaCouponRepository.deleteAll()
    jpaProductRepository.deleteAll()
  }

  @Test
  fun cannotOrderMoreThanStock() {
    val customers = List(20) { index -> Customer(0) }
    jpaCustomerRepository.saveAll(customers)

    val product = Product(0L, "A", 10, 10_000)
    jpaProductRepository.save(product)

    customers.parallelStream().forEach {
      mockMvc.post("/api/orders") {
        contentType = MediaType.APPLICATION_JSON
        content =
          "{ \"address\": \"zep\", \"customerId\": ${it.id}, \"products\": [{ \"productId\": 1, \"amount\": 1 }]}"
        header("customerId", it.id)
      }
    }

    assertEquals(10, jpaOrderProductRepository.findAll().size)
  }
}
