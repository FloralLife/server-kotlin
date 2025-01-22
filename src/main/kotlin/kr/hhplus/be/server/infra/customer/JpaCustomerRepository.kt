package kr.hhplus.be.server.infra.customer

import jakarta.persistence.LockModeType
import kr.hhplus.be.server.domain.customer.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface JpaCustomerRepository : JpaRepository<Customer, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  fun findForUpdateById(id: Long): Customer?
}
