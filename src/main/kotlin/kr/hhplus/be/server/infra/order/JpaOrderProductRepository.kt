package kr.hhplus.be.server.infra.order

import kr.hhplus.be.server.domain.order.OrderProduct
import kr.hhplus.be.server.domain.payment.PaymentStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface JpaOrderProductRepository : JpaRepository<OrderProduct, Long> {
    fun findAllByOrderId(
        orderId: Long,
        pageable: Pageable,
    ): Page<OrderProduct>

    @Query(
        """
    SELECT op.product.id
    FROM OrderProduct op
    JOIN op.order o
    JOIN o.payment pay
    WHERE pay.status = :paymentStatus
      AND pay.createdAt >= :startDate
    GROUP BY op.product.id
    ORDER BY SUM(op.amount) DESC
    LIMIT 5
  """,
    )
    fun findTop5MostPurchasedProductsInLast3Days(
        @Param("paymentStatus") paymentStatus: PaymentStatus = PaymentStatus.COMPLETED,
        @Param("startDate") startDate: LocalDateTime,
    ): List<Long>
}
