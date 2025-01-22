package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class CustomerCouponServiceTest {
  @Mock
  lateinit var customerCouponRepository: CustomerCouponRepository

  @InjectMocks
  lateinit var customerCouponService: CustomerCouponService

  @Test
  @DisplayName("존재하지 않는 유저 쿠폰 조회시 NotFoundException")
  fun getThenNotFound() {
    whenever(customerCouponRepository.findById(anyLong())).thenReturn(null)

    assertThrows(NotFoundException::class.java) { customerCouponService.get(randomId()) }
  }

  @Test
  @DisplayName("쿠폰 생성시 유저, 쿠폰정보가 저장됨")
  fun create() {
    val customer = Customer(randomId(), 10_000)
    val coupon = Coupon(randomId(), "쿠폰", 10, 10, 7)

    whenever(customerCouponRepository.save(any())).thenAnswer { invocation ->
      invocation.arguments[0] as CustomerCoupon
    }

    customerCouponService.create(customer, coupon)

    verify(customerCouponRepository).save(
      argThat {
        CustomerCouponStatus.UNUSED == this.status &&
            LocalDate.now().plusDays(7) == this.expirationDate
      },
    )
  }
}
