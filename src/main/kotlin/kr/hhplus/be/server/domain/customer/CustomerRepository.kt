package kr.hhplus.be.server.domain.customer

interface CustomerRepository {
    fun findById(id: Long): Customer?

    fun findForUpdateById(id: Long): Customer?

    fun save(customer: Customer): Customer
}
