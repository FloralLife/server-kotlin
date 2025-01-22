package kr.hhplus.be.server.application

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.application.customer.CustomerUseCase
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import kr.hhplus.be.server.exception.NotFoundException
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class CustomerUseCaseTest
@Autowired
constructor(
  private val jpaCustomerRepository: JpaCustomerRepository,
  private val jpaCustomerCouponRepository: JpaCustomerCouponRepository,
  private val jpaCouponRepository: JpaCouponRepository,
  private val customerUseCase: CustomerUseCase,
) {
  lateinit var customer: Customer
  lateinit var coupon: Coupon

  @BeforeEach
  fun setUp() {
    customer = jpaCustomerRepository.save(Customer())
    coupon = jpaCouponRepository.save(Coupon(0L, "선착순 쿠폰", 10, 10, 30))
  }

  @AfterEach
  fun cleanUp() {
    jpaCustomerCouponRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaCouponRepository.deleteAll()
  }

  @Test
  @DisplayName("존재하지 않는 유저인 경우 Not Found")
  fun issueWithNotExistCustomerThenNotFound() {
    val customerId = generateSequence { randomId() }.first { it != customer.id }

    assertThrows(NotFoundException::class.java) { customerUseCase.issueCoupon(customerId, randomId()) }
  }

  @Test
  @DisplayName("존재하지 않는 쿠폰인 경우 Not Found")
  fun issueWithNotExistCouponThenNotFound() {
    val couponId = generateSequence { randomId() }.first { it != coupon.id }

    assertThrows(NotFoundException::class.java) { customerUseCase.issueCoupon(customer.id, couponId) }
  }

  @Test
  @DisplayName("쿠폰 발행")
  fun issue() {
    val result = customerUseCase.issueCoupon(customer.id, coupon.id)

    assertEquals(customer.id, result.customerId)
    assertEquals(coupon.id, result.couponId)
    assertEquals(CustomerCouponStatus.UNUSED, result.status)
    assertEquals(LocalDate.now().plusDays(coupon.expirationPeriod.toLong()), result.expirationDate)
  }
}
