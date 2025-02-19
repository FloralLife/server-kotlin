package kr.hhplus.be.server.domain.order

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kr.hhplus.be.server.domain.product.Product

@Entity
@Table(name = "order_product",
  indexes = [Index(name = "idx_order_id_product_id_amount", columnList = "order_id, product_id, amount")]
)
class OrderProduct(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  val product: Product,
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  val order: Order,
  val amount: Int,
)
