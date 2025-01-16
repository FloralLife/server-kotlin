package kr.hhplus.be.server.e2e

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderProductRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.product.JpaProductRepository
import org.junit.jupiter.api.AfterEach
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
class CustomerCouponE2ETest
@Autowired
constructor(
  val jpaCustomerRepository: JpaCustomerRepository,
  val jpaOrderProductRepository: JpaOrderProductRepository,
  val jpaCustomerCouponRepository: JpaCustomerCouponRepository,
  var jpaCouponRepository: JpaCouponRepository,
  val jpaProductRepository: JpaProductRepository,
  val jpaOrderRepository: JpaOrderRepository,
  val mockMvc: MockMvc,
) {
  lateinit var coupon: Coupon

  @BeforeEach
  fun setUp() {
    coupon = Coupon(0L, "선착순 쿠폰", 10, 10, 30)
    jpaCouponRepository.save(coupon)
  }

  @AfterEach
  fun cleanup() {
    jpaOrderProductRepository.deleteAll()
    jpaCustomerCouponRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaCouponRepository.deleteAll()
    jpaProductRepository.deleteAll()
  }

  @Test
  @DisplayName("동시에 다수의 사람이 쿠폰을 발급받아도 정해진 재고만큼 발급된다.")
  fun issueCustomerCoupon() {
    val users = List(20) { index -> Customer(0) }
    jpaCustomerRepository.saveAll(users)

    users.parallelStream().forEach {
      mockMvc.post("/api/customers/${it.id}/coupons") {
        contentType = MediaType.APPLICATION_JSON
        content = "{ \"couponId\": ${coupon.id} }"
        header("customerId", it.id)
      }
    }

    assertEquals(10, jpaCustomerCouponRepository.findAll().filter { it.coupon.id == 1L }.size)
  }

  @Test
  @DisplayName("한사람이 여러장의 쿠폰의 발급을 요청해도 한번만 성공한다.")
  fun issueCustomerCouponOnlyOneForOneUser() {
    val customer = Customer()
    jpaCustomerRepository.save(customer)

    val results =
      List(10) { it }
        .parallelStream()
        .map {
          mockMvc
            .post("/api/customers/${customer.id}/coupons") {
              contentType = MediaType.APPLICATION_JSON
              content = "{ \"couponId\": ${coupon.id} }"
              header("customerId", customer.id)
            }.andReturn()
            .response.status
        }.toList()

    assertEquals(1, results.count { it == 200 })
    assertEquals(9, results.count { it == 409 })
  }
}
