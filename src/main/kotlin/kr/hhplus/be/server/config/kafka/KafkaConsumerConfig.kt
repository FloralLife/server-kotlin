package kr.hhplus.be.server.config.kafka

import kr.hhplus.be.server.application.payment.event.PaymentCompleteEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig {

  @Bean
  fun consumerFactory(): ConsumerFactory<String, PaymentCompleteEvent> {
    val props = mapOf<String, Any>(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
      ConsumerConfig.GROUP_ID_CONFIG to "hhp-consumer",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
    )
    val jsonDeserializer = JsonDeserializer<PaymentCompleteEvent>().apply {
      this.setRemoveTypeHeaders(false)
      this.addTrustedPackages("*")
    }
    return DefaultKafkaConsumerFactory(
      props,
      StringDeserializer(),
      jsonDeserializer
    )
  }

  @Bean
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PaymentCompleteEvent> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, PaymentCompleteEvent>()

    factory.consumerFactory = consumerFactory()
    return factory
  }
}
