package kr.hhplus.be.server

import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate

@SpringBootTest
class KafkaTest @Autowired constructor(
  private val kafkaTemplate: KafkaTemplate<String, String>
) {

  var test = false

  @KafkaListener(topics = ["topic"], groupId = "group")
  fun listen(message: String) {
    test = true
  }

  @BeforeEach
  fun setUp() {
    test = false
  }

  @Test
  @DisplayName("Spring 에 kafka 가 연동이 잘 되었는지 테스트")
  fun kafkaTest() {
    kafkaTemplate.send(ProducerRecord("topic", "test", "test")).get()
    Thread.sleep(1000)
    assertTrue(test)
  }
}
