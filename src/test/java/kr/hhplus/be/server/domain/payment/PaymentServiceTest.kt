package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponStatus
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class PaymentServiceTest {
    @Mock
    lateinit var paymentRepository: PaymentRepository

    @InjectMocks
    lateinit var paymentService: PaymentService

    val customer: Customer = Customer(1L, 1_000_000)

    val coupon: Coupon = Coupon(1L, "선착순 쿠폰", 10, 100, 30)

    val customerCoupon: CustomerCoupon =
        CustomerCoupon(1L, customer, coupon, LocalDate.now(), CustomerCouponStatus.UNUSED, null)

    val order =
        Order(
            id = randomId(),
            address = "zep",
            customer = customer,
            customerCoupon = customerCoupon,
            payment = null,
            totalPrice = 100_000,
            discountPrice = 10_000,
        )

    @Test
    @DisplayName("존재하지 않는 결제 내역 조회시 NotFoundException")
    fun getPaymentThenNotFound() {
        whenever(paymentRepository.findById(anyLong())).thenReturn(null)

        assertThrows(NotFoundException::class.java) { paymentService.get(randomId()) }
    }

    @Test
    @DisplayName("주문 Id로 조회시 결제 내역이 존재하지 않으면 null 반환")
    fun getPaymentByOrderIdThenNull() {
        whenever(paymentRepository.findByOrderId(anyLong())).thenReturn(null)

        assertEquals(null, paymentService.getByOrderId(randomId()))
    }

    @Test
    @DisplayName("결제 생성 테스트")
    fun create() {
        paymentService.create(order)

        verify(paymentRepository).save(
            argThat {
                order.id == this.order.id &&
                    90_000 == this.price
            },
        )
    }
}
