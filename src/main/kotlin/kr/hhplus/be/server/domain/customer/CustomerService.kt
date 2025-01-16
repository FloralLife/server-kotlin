package kr.hhplus.be.server.domain.customer

import kr.hhplus.be.server.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) {
    fun get(id: Long): Customer = customerRepository.findById(id) ?: throw NotFoundException(id, Customer::class.java)

    fun getWithLock(id: Long): Customer = customerRepository.findForUpdateById(id) ?: throw NotFoundException(id, Customer::class.java)

    fun create(): Customer = customerRepository.save(Customer())

    @Transactional
    fun chargePoint(
        id: Long,
        amount: Int,
    ): Customer {
        val customer = getWithLock(id)
        customer.chargePoint(amount)
        return customer
    }

    @Transactional
    fun usePoint(
        id: Long,
        amount: Int,
    ): Customer {
        val customer = getWithLock(id)
        customer.usePoint(amount)
        return customer
    }
}
