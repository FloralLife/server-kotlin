package kr.hhplus.be.server.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hhplus.be.server.domain.customer.CustomerService
import kr.hhplus.be.server.exception.NotFoundException
import kr.hhplus.be.server.exception.UnauthorizedException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class CustomerInterceptor(
  private val customerService: CustomerService,
) : HandlerInterceptor {
  override fun preHandle(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
  ): Boolean {
    // TODO: 로그인 기능 구현 후에 수정
    val customerId = request.getHeader("customerId")

    if (customerId.isNullOrBlank()) {
      throw UnauthorizedException("로그인이 필요합니다.")
    }

    try {
      customerService.get(customerId.toLong())
    } catch (e: NotFoundException) {
      throw UnauthorizedException("유효하지 않은 사용자입니다.")
    }

    return true
  }
}
