package kr.hhplus.be.server.infra.outbox

import kr.hhplus.be.server.domain.outbox.OutboxEntity
import kr.hhplus.be.server.domain.outbox.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository

interface JpaOutboxRepository : JpaRepository<OutboxEntity, Long> {
  fun findAllByEventTypeAndStatus(eventType: String, status: OutboxStatus): List<OutboxEntity>
}
