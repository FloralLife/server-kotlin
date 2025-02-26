package kr.hhplus.be.server.config.kafka

import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

  @Bean
  fun producerFactory(): ProducerFactory<String, PaymentCompleteEvent> {
    val props = hashMapOf<String, Any>(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
    )

    // DefaultKafkaProducerFactory 생성
    return DefaultKafkaProducerFactory(props)
  }

  @Bean
  fun kafkaTemplate(): KafkaTemplate<String, PaymentCompleteEvent> {
    return KafkaTemplate(producerFactory())
  }
}
