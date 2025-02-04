package kr.hhplus.be.server.application

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.application.order.OrderUseCase
import kr.hhplus.be.server.application.order.command.CreateOrderProductUCCommand
import kr.hhplus.be.server.application.order.command.CreateOrderUCCommand
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.exception.NotFoundException
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerCouponRepository
import kr.hhplus.be.server.infra.customer.JpaCustomerRepository
import kr.hhplus.be.server.infra.order.JpaOrderProductRepository
import kr.hhplus.be.server.infra.order.JpaOrderRepository
import kr.hhplus.be.server.infra.product.JpaProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class OrderUseCaseTest @Autowired constructor(
  private val jpaCustomerRepository: JpaCustomerRepository,
  private val jpaCouponRepository: JpaCouponRepository,
  private val jpaCustomerCouponRepository: JpaCustomerCouponRepository,
  private val jpaOrderRepository: JpaOrderRepository,
  private val jpaProductRepository: JpaProductRepository,
  private val jpaOrderProductRepository: JpaOrderProductRepository,
  private val orderUseCase: OrderUseCase
) {
  lateinit var customer: Customer
  lateinit var coupon: Coupon
  lateinit var customerCoupon: CustomerCoupon
  lateinit var product: Product


  @BeforeEach
  fun setUp() {
    jpaOrderProductRepository.deleteAll()
    jpaProductRepository.deleteAll()
    jpaOrderRepository.deleteAll()
    jpaCustomerCouponRepository.deleteAll()
    jpaCustomerRepository.deleteAll()
    jpaCouponRepository.deleteAll()

    customer = jpaCustomerRepository.save(Customer())
    coupon = jpaCouponRepository.save(Coupon(0L, "선착순 쿠폰", 10, 10, 30))
    customerCoupon = jpaCustomerCouponRepository.save(
      CustomerCoupon(
        customer = customer,
        coupon = coupon,
        expirationDate = LocalDate.now().plusDays(coupon.expirationPeriod.toLong()),
        usedAt = null
      )
    )
    product = jpaProductRepository.save(Product(name = "상품", stock = 10, price = 10_000))
  }

  @Test
  @DisplayName("존재하지 않는 유저면 Not Found")
  fun orderWithNotExistCustomerThenNotFound() {
    val customerId = generateSequence { randomId() }.first { it != customer.id }
    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customerId,
      customerCouponId = customerCoupon.id,
      products = listOf(CreateOrderProductUCCommand(product.id, 5)),
    )

    assertThrows(NotFoundException::class.java) { orderUseCase.order(orderCommand) }
  }

  @Test
  @DisplayName("존재하지 않는 유저 쿠폰이면 Not Found")
  fun orderWithNotExistCustomerCouponThenNotFound() {
    val customerCouponId = generateSequence { randomId() }.first { it != customerCoupon.id }
    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customer.id,
      customerCouponId = customerCouponId,
      products = listOf(CreateOrderProductUCCommand(product.id, 5)),
    )

    assertThrows(NotFoundException::class.java) { orderUseCase.order(orderCommand) }
  }

  @Test
  @DisplayName("존재하지 않는 상품을 주문하면 Not Found")
  fun orderWithNotExistProductThenNotFound() {
    val productId = generateSequence { randomId() }.first { it != product.id }
    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customer.id,
      customerCouponId = customerCoupon.id,
      products = listOf(
        CreateOrderProductUCCommand(product.id, 5),
        CreateOrderProductUCCommand(productId, 5)
      ),
    )

    assertThrows(NotFoundException::class.java) { orderUseCase.order(orderCommand) }
  }

  @Test
  @DisplayName("만료된 쿠폰을 사용하는 경우 IllegalArgumentException")
  fun orderWithExpiredCouponThenIllegalArgumentException() {
    jpaCustomerCouponRepository.deleteAll()
    val expiredCoupon = jpaCustomerCouponRepository.save(
      CustomerCoupon(
        customer = customer,
        coupon = coupon,
        expirationDate = LocalDate.now().minusDays(1),
        usedAt = null
      )
    )

    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customer.id,
      customerCouponId = expiredCoupon.id,
      products = listOf(
        CreateOrderProductUCCommand(product.id, 5)
      )
    )

    assertThrows(IllegalArgumentException::class.java) { orderUseCase.order(orderCommand) }
  }

  @Test
  @DisplayName("상품의 재고보다 많은 양을 구매한 경우 IllegalStateException")
  fun orderMoreThanStockThenIllegalStateException() {
    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customer.id,
      customerCouponId = customerCoupon.id,
      products = listOf(
        CreateOrderProductUCCommand(product.id, 20)
      )
    )

    assertThrows(IllegalStateException::class.java) { orderUseCase.order(orderCommand) }
  }

  @Test
  @DisplayName("주문 이후 상품의 개수 감소 및 쿠폰 사용처리")
  fun order() {
    val orderCommand = CreateOrderUCCommand(
      address = "Hhplus",
      customerId = customer.id,
      customerCouponId = customerCoupon.id,
      products = listOf(
        CreateOrderProductUCCommand(product.id, 5)
      )
    )

    val result = orderUseCase.order(orderCommand)

    val resultCustomerCoupon = jpaCustomerCouponRepository.findById(result.customerCouponId!!).get()
    val resultProduct = jpaProductRepository.findById(result.products[0].productId).get()

    assertEquals(50000, result.totalPrice)
    assertEquals(5000, result.discountPrice)
    assertEquals(CustomerCouponStatus.USED, resultCustomerCoupon.status)
    assertEquals(5, resultProduct.stock)
  }
}
