package kr.hhplus.be.server.application.payment

import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import kr.hhplus.be.server.domain.outbox.OutboxRepository
import kr.hhplus.be.server.domain.outbox.OutboxStatus
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class PaymentEventConsumer(
  private val outboxRepository: OutboxRepository
) {
  @KafkaListener(
    topics = ["payment-complete"],
    groupId = "hhp-consumer",
    containerFactory = "kafkaListenerContainerFactory"
  )
  fun consume(event: PaymentCompleteEvent) {
    val outbox = outboxRepository.findById(event.eventId)!!
    outbox.status = OutboxStatus.SUCCESS
    outboxRepository.save(outbox)
  }
}
