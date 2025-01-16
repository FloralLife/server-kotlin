package kr.hhplus.be.server.interceptor

import kr.hhplus.be.server.TestUtils.randomId
import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerService
import kr.hhplus.be.server.exception.NotFoundException
import kr.hhplus.be.server.exception.UnauthorizedException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

@ExtendWith(MockitoExtension::class)
class CustomerInterceptorTest {
  @Mock
  lateinit var customerService: CustomerService

  @InjectMocks
  lateinit var customerInterceptor: CustomerInterceptor

  private lateinit var request: MockHttpServletRequest
  private lateinit var response: MockHttpServletResponse

  @BeforeEach
  fun setUp() {
    request = MockHttpServletRequest()
    response = MockHttpServletResponse()
  }

  @Test
  @DisplayName("customerId가 헤더에 없을 때 Unauthorized")
  fun preHandleWithNoHeaderThenUnauthorized() {
    assertThrows(UnauthorizedException::class.java) {
      customerInterceptor.preHandle(request, response, Any())
    }
  }

  @Test
  @DisplayName("customerId에 있는 값이 유효하지 않은 ID이면 Unauthorized")
  fun preHandleWithInvalidCustomerThenUnauthorized() {
    val customerId = randomId()
    request.addHeader("customerId", customerId)
    whenever(customerService.get(customerId)).thenThrow(NotFoundException::class.java)

    assertThrows(UnauthorizedException::class.java) {
      customerInterceptor.preHandle(request, response, Any())
    }
  }

  @Test
  @DisplayName("유효한 customer ID가 헤더이 있으면 true")
  fun preHandle() {
    val customerId = randomId()
    request.addHeader("customerId", customerId)
    whenever(customerService.get(customerId)).thenReturn(Customer(customerId))

    assertTrue(customerInterceptor.preHandle(request, response, Any()))
  }
}
