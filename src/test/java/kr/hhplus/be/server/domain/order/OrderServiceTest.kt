package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import kr.hhplus.be.server.domain.order.command.CreateOrderCommand
import kr.hhplus.be.server.domain.order.command.CreateOrderProductCommand
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.exception.HhpNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {
    @Mock
    lateinit var orderRepository: OrderRepository

    @Mock
    lateinit var orderProductRepository: OrderProductRepository

    @InjectMocks
    lateinit var orderService: OrderService

    val product1 = Product(1L, "A", 20, 10_000)
    val product2 = Product(2L, "B", 20, 20_000)

    val customer: Customer = Customer(1L, 1_000_000)

    val coupon: Coupon = Coupon(1L, "선착순 쿠폰", 10, 100, 30)

    val customerCoupon: CustomerCoupon =
        CustomerCoupon(1L, customer, coupon, LocalDate.now(), CustomerCouponStatus.UNUSED, null)

    @Test
    @DisplayName("존재하지 않는 주문 조회시 에러 발생")
    fun getOrderThenNotFound() {
        whenever(orderRepository.findById(anyLong())).thenReturn(null)

        assertThrows(HhpNotFoundException::class.java) { orderService.get(randomId()) }
    }

    @Test
    @DisplayName("주문 생성시 Order, OrderProduct모두 정상적으로 생성")
    fun create() {
        val command =
            CreateOrderCommand(
                address = "zep",
                customer = customer,
                customerCoupon = customerCoupon,
                discountRate = coupon.discountRate,
                products =
                    listOf(
                        CreateOrderProductCommand(
                            product1,
                            1,
                        ),
                        CreateOrderProductCommand(
                            product2,
                            2,
                        ),
                    ),
            )

        val result = orderService.create(command)

        assertEquals(command.products.size, result.products.size)
        assertEquals(5000, result.discountPrice)
    }
}
