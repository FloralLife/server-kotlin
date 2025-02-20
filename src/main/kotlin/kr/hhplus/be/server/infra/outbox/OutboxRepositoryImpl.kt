package kr.hhplus.be.server.infra.outbox

import kr.hhplus.be.server.domain.outbox.OutboxEntity
import kr.hhplus.be.server.domain.outbox.OutboxRepository
import kr.hhplus.be.server.domain.outbox.OutboxStatus
import org.springframework.stereotype.Repository

@Repository
class OutboxRepositoryImpl(
  private val jpaOutboxRepository: JpaOutboxRepository
) : OutboxRepository {
  override fun findById(id: Long): OutboxEntity? {
    return jpaOutboxRepository.findById(id).orElse(null)
  }

  override fun findAllByEventTypeAndStatus(eventType: String, status: OutboxStatus): List<OutboxEntity> {
    return jpaOutboxRepository.findAllByEventTypeAndStatus(eventType, status)
  }

  override fun save(outbox: OutboxEntity): OutboxEntity {
    return jpaOutboxRepository.save(outbox)
  }
}
