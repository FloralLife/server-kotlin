package kr.hhplus.be.server.operation

import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.payment.Payment
import kr.hhplus.be.server.domain.payment.PaymentStatus
import kr.hhplus.be.server.domain.payment.PaymentType
import kr.hhplus.be.server.domain.product.Product
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDateTime
import kotlin.random.Random

@SpringBootTest
class InsertDummyData @Autowired constructor(
  private val jdbcTemplate: JdbcTemplate
) {

  @BeforeEach
  fun setUp() {
    jdbcTemplate.execute("DELETE FROM payment")
    jdbcTemplate.execute("DELETE FROM order_product")
    jdbcTemplate.execute("DELETE FROM `order`")
    jdbcTemplate.execute("DELETE FROM product")
    jdbcTemplate.execute("DELETE FROM customer")
  }

  @Test
  @DisplayName("Order, OrderProduct, Payment에 더미 데이터를 추가합니다.")
  fun insertDummyData() {
    val customers = (1..100).map { Customer(it.toLong(), 0) }
    customers.forEach { customer ->
      jdbcTemplate.update("INSERT INTO customer (id, point) VALUES (?, ?)", customer.id, customer.point)
    }

    val products = (1..100).map { Product(id = it.toLong(), name = "Product $it", stock = 100, price = 1000) }
    products.forEach { product ->
      val createdAt = getRandomDateTimeWithinLast10Days()
      jdbcTemplate.update(
        "INSERT INTO product (id, name, stock, price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)",
        product.id,
        product.name,
        product.stock,
        product.price,
        createdAt,
        createdAt
      )
    }


    val orders = (1..1000000).map {
      Order(
        id = it.toLong(),
        address = "Address $it",
        totalPrice = 10000,
        customer = customers.random(),
        customerCoupon = null,
        discountPrice = 0
      )
    }
    orders.forEach { order ->
      val createdAt = getRandomDateTimeWithinLast10Days()
      jdbcTemplate.update(
        "INSERT INTO `order` (id, address, total_price, customer_id, discount_price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
        order.id,
        order.address,
        order.totalPrice,
        order.customer.id,
        order.discountPrice,
        createdAt,
        createdAt
      )
    }

    val orderProducts = (1..2000000).map {
      OrderProduct(
        product = products.random(),
        order = orders.random(),
        amount = (1..10).random()
      )
    }
    orderProducts.forEach { orderProduct ->
      val createdAt = getRandomDateTimeWithinLast10Days()
      jdbcTemplate.update(
        "INSERT INTO order_product (product_id, order_id, amount) VALUES (?, ?, ?)",
        orderProduct.product.id, orderProduct.order.id, orderProduct.amount
      )
    }

    val payments = (1..1000000).map {
      Payment(
        id = it.toLong(),
        order = orders.random(),
        status = PaymentStatus.COMPLETED,
        type = PaymentType.POINT,
        price = 10000
      )
    }

    payments.forEach { payment ->
      val createdAt = getRandomDateTimeWithinLast10Days()
      jdbcTemplate.update(
        "INSERT INTO payment (id, order_id, status, type, price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
        payment.id,
        payment.id,
        payment.status.name,
        payment.type.name,
        payment.price,
        createdAt,
        createdAt
      )
    }
  }

  private fun getRandomDateTimeWithinLast10Days(): LocalDateTime {
    val now = LocalDateTime.now()
    val randomDays = Random.nextLong(0, 10)
    val randomSeconds = Random.nextLong(0, 86400) // 86400 seconds in a day
    return now.minusDays(randomDays).minusSeconds(randomSeconds)
  }
}
