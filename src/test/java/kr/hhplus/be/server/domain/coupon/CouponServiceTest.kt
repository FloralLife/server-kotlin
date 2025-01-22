package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.exception.NotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {
  @Mock
  lateinit var couponRepository: CouponRepository

  @InjectMocks
  lateinit var couponService: CouponService

  @Test
  @DisplayName("존재하지 않는 쿠폰 조회시 NotFoundException")
  fun getUserThenNotFound() {
    whenever(couponRepository.findById(anyLong())).thenReturn(null)

    assertThrows(NotFoundException::class.java) { couponService.get(randomId()) }
  }

  @Test
  @DisplayName("존재하지 않는 쿠폰 조회시 NotFoundException")
  fun getUserWithLockThenNotFound() {
    whenever(couponRepository.findForUpdateById(anyLong())).thenReturn(null)

    assertThrows(NotFoundException::class.java) { couponService.getWithLock(randomId()) }
  }
}
