package kr.hhplus.be.server.domain.outbox.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import kr.hhplus.be.server.domain.outbox.OutboxEntity
import kr.hhplus.be.server.domain.outbox.OutboxRepository
import kr.hhplus.be.server.domain.outbox.OutboxStatus
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.kafka.core.KafkaTemplate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PaymentCompleteEventOutboxSchedulerTest {
  @Mock
  lateinit var outboxRepository: OutboxRepository

  @Mock
  lateinit var objectMapper: ObjectMapper

  @Mock
  lateinit var kafkaTemplate: KafkaTemplate<String, PaymentCompleteEvent>

  @InjectMocks
  lateinit var paymentCompleteEventOutboxScheduler: PaymentCompleteEventOutboxScheduler


  @Test
  @DisplayName("5분이 지난 outbox를 조회하고 kafka로 전송한다.")
  fun publishOutbox() {
    val outbox = OutboxEntity(
      id = 1,
      eventType = PaymentCompleteEvent::class.simpleName!!,
      payload = "{}",
      status = OutboxStatus.PENDING,
      createdAt = LocalDateTime.now().minusMinutes(6)
    )
    val recentOutbox = OutboxEntity(
      id = 2,
      eventType = PaymentCompleteEvent::class.simpleName!!,
      payload = "{}",
      status = OutboxStatus.PENDING,
      createdAt = LocalDateTime.now()
    )

    val paymentCompleteEvent = PaymentCompleteEvent(0, 0, 0, listOf())

    // given
    whenever(outboxRepository.findAllByEventTypeAndStatus(PaymentCompleteEvent::class.simpleName!!, OutboxStatus.PENDING))
      .thenReturn(listOf(outbox, recentOutbox))
    whenever(objectMapper.readValue(outbox.payload, PaymentCompleteEvent::class.java))
      .thenReturn(paymentCompleteEvent)

    // when
    paymentCompleteEventOutboxScheduler.publishOutbox()

    // then
    verify(kafkaTemplate, times(1)).send("payment-complete", paymentCompleteEvent)
  }
}
