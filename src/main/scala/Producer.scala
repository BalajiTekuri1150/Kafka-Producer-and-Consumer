import java.util.Properties
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.{Logger, LoggerFactory}
object Producer {
  val logger: Logger = LoggerFactory.getLogger(Producer.getClass)
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String,String](props)
    val topic = "kafka-topic1"
    //send messages to kafka using synchronous messaging system
    for(i <- 1 to 50) {
      val producerRecord = new ProducerRecord[String,String](topic, i.toString)
      try {
        producer.send((producerRecord)).get()
        logger.info(s"Message $i sent to $topic successfully")
      }catch {
        case e:Exception => logger.error(s"Exception occurred while sending message to topic $topic")
      }
    }

    //sending messages to kafka using asynchronous messaging system
    for(i <- 51 to 100){
      val producerRecord = new ProducerRecord[String,String](topic,i.toString)
      try{
        producer.send(producerRecord,new Callback {
          override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
            if(exception != null) logger.error(s"Exception occurred while sending a message to kafka")
            else logger.info(s"successfully sent msg $i to kafka topic $topic")
          }
        })
      }catch {
        case e:Exception => logger.error(e.getMessage.toString)
      }
    }

  }
}
