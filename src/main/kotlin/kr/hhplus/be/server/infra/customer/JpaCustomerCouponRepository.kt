package kr.hhplus.be.server.infra.customer

import kr.hhplus.be.server.domain.customer.CustomerCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface JpaCustomerCouponRepository : JpaRepository<CustomerCoupon, Long?> {
  fun findAllByCustomerId(customerId: Long): List<CustomerCoupon>
}
