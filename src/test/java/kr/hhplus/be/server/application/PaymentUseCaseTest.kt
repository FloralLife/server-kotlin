package kr.hhplus.be.server.application

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.application.payment.PaymentUseCase
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderStatus
import kr.hhplus.be.server.exception.NotFoundException
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.payment.JpaPaymentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class PaymentUseCaseTest @Autowired constructor(
  private val jpaCustomerRepository: JpaCustomerRepository,
  private val jpaOrderRepository: JpaOrderRepository,
  private val jpaPaymentRepository: JpaPaymentRepository,
  private val paymentUseCase: PaymentUseCase,
  private val jdbcTemplate: JdbcTemplate,
) {
  @PersistenceContext
  private lateinit var entityManager: EntityManager
  lateinit var customer: Customer
  lateinit var order: Order


  @BeforeEach
  fun setUp() {
    // persistent instance references an unsaved transient instance of 'kr.hhplus.be.server.domain.payment.Payment' (save the transient instance before flushing)
    // 에러로 인해 jdbc 직접 사용
    jdbcTemplate.update("DELETE FROM payment")
    jdbcTemplate.update("DELETE FROM `order`")
    jdbcTemplate.update("DELETE FROM customer")

    customer = jpaCustomerRepository.save(Customer(point = 100_000))
    order = jpaOrderRepository.save(
      Order(
        address = "Hhplus",
        customer = customer,
        status = OrderStatus.PAYMENT_WAITING,
        totalPrice = 50_000,
        discountPrice = 5_000,
      )
    )
  }

  @Test
  @DisplayName("존재하지 않는 유저 요청시 Not Found")
  fun payWithNotExistCustomerThenNotFound() {
    val customerId = generateSequence { randomId() }.first { it != customer.id }

    assertThrows(NotFoundException::class.java) { paymentUseCase.pay(customerId, order.id) }
  }

  @Test
  @DisplayName("존재하지 않는 주문 요청시 Not Found")
  fun payWithNotExistOrderThenNotFound() {
    val orderId = generateSequence { randomId() }.first { it != order.id }

    assertThrows(NotFoundException::class.java) { paymentUseCase.pay(customer.id, orderId) }
  }

  @Test
  @DisplayName("사용자 잔액보다 많은 금액 요청시 IllegalStateException")
  fun payMoreThanBalanceThenIllegalArgumentException() {
    order.totalPrice = 200_000
    jpaOrderRepository.save(order)

    assertThrows(IllegalArgumentException::class.java) { paymentUseCase.pay(customer.id, order.id) }
  }

  @Test
  @DisplayName("주문 성공시 사용자 잔액 감소 및 주문 상태 변경")
  fun pay() {
    val result = paymentUseCase.pay(customer.id, order.id)

    val resultCustomer = jpaCustomerRepository.findById(customer.id).get()
    val resultOrder = jpaOrderRepository.findById(order.id).get()

    assertEquals(result.id, resultOrder.payment!!.id)
    assertEquals(55_000, resultCustomer.point)
    assertEquals(OrderStatus.PAYMENT_COMPLETED, resultOrder.status)
  }
}
