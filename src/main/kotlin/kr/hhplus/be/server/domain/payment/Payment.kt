package kr.hhplus.be.server.domain.payment

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.BaseEntity
import kr.hhplus.be.server.domain.order.Order
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "payment")
@EntityListeners(AuditingEntityListener::class)
class Payment(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  val order: Order,

  val status: PaymentStatus = PaymentStatus.WAITING,

  val type: PaymentType = PaymentType.POINT,

  val price: Int,

  ) : BaseEntity() {

}
