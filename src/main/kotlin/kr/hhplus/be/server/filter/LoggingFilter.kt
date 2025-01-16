package kr.hhplus.be.server.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingFilter : OncePerRequestFilter() {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    val requestWrapper = ContentCachingRequestWrapper(request)
    val responseWrapper = ContentCachingResponseWrapper(response)

    try {
      val method = requestWrapper.method
      val requestUrl = requestWrapper.requestURL

      log.info(
        "HTTP Request: method=$method, url=$requestUrl",
      )

      val startTime = System.currentTimeMillis()
      filterChain.doFilter(requestWrapper, responseWrapper)
      val endTime = System.currentTimeMillis()

      log.info(
        "HTTP Response: method=$method, url=$requestUrl" +
            ", status: ${responseWrapper.status}, elapsed: ${(endTime - startTime)}ms",
      )
    } finally {
      responseWrapper.copyBodyToResponse()
    }
  }
}
