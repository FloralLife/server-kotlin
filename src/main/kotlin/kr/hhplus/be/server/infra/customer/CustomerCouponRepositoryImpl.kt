package kr.hhplus.be.server.infra.customer

import kr.hhplus.be.server.domain.customer.CustomerCoupon
import kr.hhplus.be.server.domain.customer.CustomerCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CustomerCouponRepositoryImpl(
  private val jpaCustomerCouponRepository: JpaCustomerCouponRepository,
) : CustomerCouponRepository {
  override fun findById(id: Long): CustomerCoupon? = jpaCustomerCouponRepository.findByIdOrNull(id)

  override fun findAllByCustomerId(customerId: Long): List<CustomerCoupon> =
    jpaCustomerCouponRepository.findAllByCustomerId(customerId)

  override fun saveAndFlush(customerCoupon: CustomerCoupon): CustomerCoupon =
    jpaCustomerCouponRepository.saveAndFlush(customerCoupon)
}
