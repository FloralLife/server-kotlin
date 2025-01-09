package kr.hhplus.be.server.domain.payment

interface PaymentRepository {
    fun findById(id: Long): Payment?

    fun findByOrderId(orderId: Long): Payment?

    fun save(payment: Payment): Payment
}
