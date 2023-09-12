import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.{Logger, LoggerFactory}

import java.util.Properties
import java.time.Duration.ofMillis
import scala.collection.JavaConverters._
object Consumer {
  val logger: Logger = LoggerFactory.getLogger(Consumer.getClass)
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers","localhost:9092")
    props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id","topic1")
    val consumer = new KafkaConsumer[String,String](props)
    val topic = List("kafka-topic1")
    consumer.subscribe(topic.asJava)
    try{
      while(true){
        val records = consumer.poll(ofMillis(100))
        for(record <- records.asScala) logger.info("key: "+record.key()+" value : "+record.value())
      }
    }catch{
      case e:Exception => logger.error(e.getMessage)
    }finally {
      consumer.close()
    }
  }
}
