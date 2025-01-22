package kr.hhplus.be.server.infra.customer

import kr.hhplus.be.server.domain.customer.Customer
import kr.hhplus.be.server.domain.customer.CustomerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CustomerRepositoryImpl(
  private val jpaCustomerRepository: JpaCustomerRepository,
) : CustomerRepository {
  override fun findById(id: Long): Customer? = jpaCustomerRepository.findByIdOrNull(id)

  override fun findForUpdateById(id: Long): Customer? = jpaCustomerRepository.findForUpdateById(id)

  override fun save(customer: Customer): Customer = jpaCustomerRepository.save(customer)
}
