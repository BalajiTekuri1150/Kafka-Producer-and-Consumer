import java.util.Properties
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.{Logger, LoggerFactory}
import com.typesafe.config.ConfigFactory

object Producer extends App {
  val logger: Logger = LoggerFactory.getLogger(Producer.getClass)

  private val config = ConfigFactory.load()
  private val kafkaConfig = config.getConfig("kafka")

  private val bootstrapServers = kafkaConfig.getString("bootstrap-servers")
  private val keySerializer = kafkaConfig.getString("key-serializer")
  private val valueSerializer = kafkaConfig.getString("value-serializer")

  val props = new Properties()
  props.put("bootstrap.servers", bootstrapServers)
  props.put("key.serializer", keySerializer)
  props.put("value.serializer", valueSerializer)

  private val producer = new KafkaProducer[String, String](props)
  val topic = "kafka-topic1"

  //send messages to kafka using synchronous messaging system
  for (i <- 1 to 50) {
    val key = if (i % 2 == 0) "Even" else "Odd"
    val producerRecord = new ProducerRecord[String, String](topic, key, i.toString)
    try {
      producer.send((producerRecord)).get()
      logger.info(s"Message $i sent to $topic successfully")
    } catch {
      case e: Exception => logger.error(s"Exception occurred while sending message to topic $topic")
    }
  }

  //sending messages to kafka using asynchronous messaging system
  for (i <- 51 to 100) {
    val key = if (i % 2 == 0) "Even" else "Odd"
    val producerRecord = new ProducerRecord[String, String](topic, key, i.toString)
    try {
      producer.send(producerRecord, new Callback {
        override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
          if (exception != null) logger.error(s"Exception occurred while sending a message to kafka")
          else logger.info(s"successfully sent msg $i to kafka topic $topic")
        }
      })
    } catch {
      case e: Exception => logger.error(e.getMessage.toString)
    }
  }

}
