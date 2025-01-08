package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.exception.HhpNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomerService(
  private val customerRepository: CustomerRepository
) {
  fun get(id: Long): Customer {
    return customerRepository.findById(id) ?: throw HhpNotFoundException(id, Customer::class.java)
  }

  fun getWithLock(id: Long): Customer {
    return customerRepository.findForUpdateById(id) ?: throw HhpNotFoundException(id, Customer::class.java)
  }

  fun chargePoint(id: Long, amount: Int): Customer {
    val customer = get(id)
    customer.chargePoint(amount)
    return customer
  }

  fun usePoint(id: Long, amount: Int): Customer {
    val customer = get(id)
    customer.usePoint(amount)
    return customer
  }
}
