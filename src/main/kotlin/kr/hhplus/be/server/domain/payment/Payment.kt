package kr.hhplus.be.server.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.BaseEntity
import kr.hhplus.be.server.domain.order.Order
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(
  name = "payment",
  indexes = [Index(name = "idx_created_at_status_order_id", columnList = "created_at, status, order_id")]
)
@EntityListeners(AuditingEntityListener::class)
class Payment(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  val order: Order,
  @Enumerated(EnumType.STRING)
  var status: PaymentStatus = PaymentStatus.COMPLETED,
  @Enumerated(EnumType.STRING)
  val type: PaymentType = PaymentType.POINT,
  val price: Int,
) : BaseEntity() {
  init {
    require(price >= 0) { "Price must not be negative" }
  }

  fun cancel() {
    status = PaymentStatus.CANCELLED
  }
}
