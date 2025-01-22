package kr.hhplus.be.server.application

import kr.hhplus.be.server.application.product.ProductUseCase
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.order.OrderStatus
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderProductRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.payment.JpaPaymentRepository
import kr.hhplus.be.server.infra.product.JpaProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductUseCaseTest @Autowired constructor(
  private val jpaCustomerRepository: JpaCustomerRepository,
  private val jpaOrderRepository: JpaOrderRepository,
  private val jpaProductRepository: JpaProductRepository,
  private val jpaOrderProductRepository: JpaOrderProductRepository,
  private val jpaPaymentRepository: JpaPaymentRepository,
  private val productUseCase: ProductUseCase,
) {
  lateinit var customer: Customer
  lateinit var order: Order


  @BeforeEach
  fun setUp() {
    jpaOrderProductRepository.deleteAll()
    jpaProductRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerRepository.deleteAll()

    customer = jpaCustomerRepository.save(Customer())
    order = jpaOrderRepository.save(
      Order(
        address = "Hhplus",
        customer = customer,
        status = OrderStatus.PAYMENT_COMPLETED
      )
    )
  }

  @Test
  fun top5() {

    val product1 = jpaProductRepository.save(Product(name = "상품1", stock = 10, price = 10_000))
    val product2 = jpaProductRepository.save(Product(name = "상품2", stock = 10, price = 10_000))
    val product3 = jpaProductRepository.save(Product(name = "상품3", stock = 10, price = 10_000))
    val product4 = jpaProductRepository.save(Product(name = "상품4", stock = 10, price = 10_000))
    val product5 = jpaProductRepository.save(Product(name = "상품5", stock = 10, price = 10_000))
    val product6 = jpaProductRepository.save(Product(name = "상품6", stock = 10, price = 10_000))

    jpaOrderProductRepository.saveAll(
      listOf(
        OrderProduct(
          product = product2, order = order, amount = 8
        ),
        OrderProduct(
          product = product5, order = order, amount = 2
        ),
        OrderProduct(
          product = product3, order = order, amount = 6
        ),
        OrderProduct(
          product = product4, order = order, amount = 4
        ),
        OrderProduct(
          product = product1, order = order, amount = 10
        )
      )
    )
    jpaPaymentRepository.save(Payment(order = order, price = 0))

    val result = productUseCase.top5For3Days()

    assertEquals(product1.id, result[0].id)
    assertEquals(product2.id, result[1].id)
    assertEquals(product3.id, result[2].id)
    assertEquals(product4.id, result[3].id)
    assertEquals(product5.id, result[4].id)
    assertFalse(result.map { it.id }.contains(product6.id))
  }
}
