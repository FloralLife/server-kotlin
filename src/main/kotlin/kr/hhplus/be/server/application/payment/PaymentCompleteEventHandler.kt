package kr.hhplus.be.server.application.payment

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import kr.hhplus.be.server.domain.outbox.OutboxEntity
import kr.hhplus.be.server.domain.outbox.OutboxRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentCompleteEventHandler(
  private val outboxRepository: OutboxRepository,
  private val kafkaTemplate: KafkaTemplate<String, PaymentCompleteEvent>,
  private val objectMapper: ObjectMapper
) {
  @TransactionalEventListener(value = [PaymentCompleteEvent::class], phase = TransactionPhase.BEFORE_COMMIT)
  fun saveOutbox(event: PaymentCompleteEvent) {
    val outbox = outboxRepository.save(
      OutboxEntity(
        eventType = event::class.simpleName!!,
        payload = objectMapper.writeValueAsString(event),
      )
    )
    event.eventId = outbox.id
  }

  @TransactionalEventListener(value = [PaymentCompleteEvent::class], phase = TransactionPhase.AFTER_COMMIT)
  fun sendPaymentInfo(event: PaymentCompleteEvent) {
    kafkaTemplate.send("payment-complete", event)
  }
}
