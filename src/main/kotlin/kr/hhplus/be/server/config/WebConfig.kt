package kr.hhplus.be.server.config

import kr.hhplus.be.server.interceptor.CustomerInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
  private val customerInterceptor: CustomerInterceptor,
) : WebMvcConfigurer {
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(customerInterceptor).addPathPatterns("/**")
  }
}
