import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.{Logger, LoggerFactory}

import java.util.Properties
import java.time.Duration.ofMillis
import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory

object Consumer extends App {
  val logger: Logger = LoggerFactory.getLogger(Consumer.getClass)
  private val config = ConfigFactory.load()
  private val kafkaConfig = config.getConfig("kafka")

  private val bootstrapServers = kafkaConfig.getString("bootstrap-servers")
  private val keySerializer = kafkaConfig.getString("key-serializer")
  private val valueSerializer = kafkaConfig.getString("value-serializer")
  private val groupId = kafkaConfig.getString("group-id")
  val props = new Properties()
  props.put("bootstrap.servers", bootstrapServers)
  props.put("key.deserializer", keySerializer)
  props.put("value.deserializer", valueSerializer)
  props.put("group.id", groupId)
  private val consumer = new KafkaConsumer[String, String](props)
  val topic = List("kafka-topic1")
  consumer.subscribe(topic.asJava)
  try {
    while (true) {
      val records = consumer.poll(ofMillis(100))
      for (record <- records.asScala) logger.info("key: " + record.key() + " value : " + record.value())
    }
  } catch {
    case e: Exception => logger.error(e.getMessage)
  } finally {
    consumer.close()
  }
}
