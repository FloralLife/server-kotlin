package kr.hhplus.be.server.e2e

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerCouponRepository
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
import java.time.LocalDate

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
  lateinit var coupon: Coupon
  lateinit var product: Product

  @BeforeEach
  fun setUp() {
    jpaOrderProductRepository.deleteAll()
    jpaCustomerCouponRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaCouponRepository.deleteAll()
    jpaProductRepository.deleteAll()

    coupon = Coupon(
      name = "선착순 쿠폰",
      discountRate = 10,
      stock = 100,
      expirationPeriod = 30
    )
    jpaCouponRepository.save(coupon)

    product = Product(0L, "A", 10, 10_000)
    jpaProductRepository.save(product)
  }

  @Test
  @DisplayName("10개의 재고가 있는 상품에 동시에 1개 상품을 주문하는 주문이 20번 들어오면 10번만 성공")
  fun orderMultipleTimesThenSuccessOnlyUpToStock() {
    val customers = List(20) { _ -> Customer(0) }
    jpaCustomerRepository.saveAll(customers)

    customers.parallelStream().forEach {
      mockMvc.post("/api/orders") {
        contentType = MediaType.APPLICATION_JSON
        content = """
          { 
            "address": "zep",
            "customerId": ${it.id},
            "products": [{ "productId": ${product.id}, "amount": 1 }]
          }
          """.trimIndent()
        header("customerId", it.id)
      }
    }

    assertEquals(10, jpaOrderProductRepository.findAll().size)
  }

  @Test
  @DisplayName("같은 쿠폰으로는 한번밖에 주문하지 못한다.")
  fun orderWithSameCouponThenSuccessOnce() {
    val customer = Customer()
    jpaCustomerRepository.save(customer)

    val customerCoupon = CustomerCoupon(
      customer = customer,
      coupon = coupon,
      expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
      usedAt = null
    )
    jpaCustomerCouponRepository.save(customerCoupon)

    val result = List(10) { it }.parallelStream().map {
      mockMvc.post("/api/orders") {
        contentType = MediaType.APPLICATION_JSON
        content = """
          {
            "address": "zep",
            "customerId": ${customer.id},
            "customerCouponId": ${customerCoupon.id},
            "products": [{ "productId": ${product.id}, "amount": 1 }]
          }
          """.trimIndent()
        header("customerId", customer.id)
      }.andReturn().response.status
    }.toList()

    assertEquals(1, result.count { it == 200 })
  }
}
