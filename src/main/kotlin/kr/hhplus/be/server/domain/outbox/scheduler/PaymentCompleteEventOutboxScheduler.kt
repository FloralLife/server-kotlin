package kr.hhplus.be.server.domain.outbox.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import kr.hhplus.be.server.domain.outbox.OutboxRepository
import kr.hhplus.be.server.domain.outbox.OutboxStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PaymentCompleteEventOutboxScheduler(
  private val outboxRepository: OutboxRepository,
  private val objectMapper: ObjectMapper,
  private val kafkaTemplate: KafkaTemplate<String, PaymentCompleteEvent>,
) {

  val typeMap = mapOf(
    PaymentCompleteEvent::class.simpleName to PaymentCompleteEvent::class.java,
  )

  @Scheduled(cron = "0 0/1 * * * ?")
  fun publishOutbox() {
    val fiveMinutesAgo = LocalDateTime.now().minusMinutes(5)

    val failedOutboxes =
      outboxRepository.findAllByEventTypeAndStatus(
        PaymentCompleteEvent::class.simpleName!!,
        OutboxStatus.PENDING
      )
        .filter { it.createdAt.isBefore(fiveMinutesAgo) }

    failedOutboxes.forEach {
      val event = objectMapper.readValue(it.payload, typeMap[it.eventType])
      event.eventId = it.id
      kafkaTemplate.send("payment-complete", event)
    }
  }
}
