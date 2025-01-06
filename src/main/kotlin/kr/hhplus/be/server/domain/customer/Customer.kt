package kr.hhplus.be.server.domain.customer

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "customer")
class Customer(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,

  val point: Int
) {
}
