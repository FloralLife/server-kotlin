package kr.hhplus.be.server.infra.customer

import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CustomerCouponRepositoryImpl(
  private val jpaCustomerCouponRepository: JpaCustomerCouponRepository
) : CustomerCouponRepository {
  override fun findById(id: Long): CustomerCoupon? {
    return jpaCustomerCouponRepository.findByIdOrNull(id)
  }

  override fun findAllByCustomerId(customerId: Long): List<CustomerCoupon> {
    return jpaCustomerCouponRepository.findAllByCustomerId(customerId)
  }

  override fun save(customerCoupon: CustomerCoupon): CustomerCoupon {
    return jpaCustomerCouponRepository.save(customerCoupon)
  }
}
