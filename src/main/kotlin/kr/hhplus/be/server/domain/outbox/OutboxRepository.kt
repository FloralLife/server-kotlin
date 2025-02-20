package kr.hhplus.be.server.domain.outbox

interface OutboxRepository {
  fun findById(id: Long): OutboxEntity?

  fun findAllByStatus(status: OutboxStatus): List<OutboxEntity>

  fun save(outbox: OutboxEntity): OutboxEntity
}
