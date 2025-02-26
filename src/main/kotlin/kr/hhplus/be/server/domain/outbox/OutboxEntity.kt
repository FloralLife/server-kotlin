package kr.hhplus.be.server.domain.outbox

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "outbox")
class OutboxEntity(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0L,
  val eventType: String,
  @Lob
  val payload: String,
  @Enumerated(EnumType.STRING)
  var status: OutboxStatus = OutboxStatus.PENDING,
  val createdAt: LocalDateTime = LocalDateTime.now(),
)
