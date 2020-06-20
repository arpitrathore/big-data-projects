package org.arpit.scala.spark.stream01.avro

import java.util.Date

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericData
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.arpit.scala.spark.stream00.common.{LoggerUtil, SchemaUtil}

object Avro01LogConsole extends App {

  val BATCH_DURATION = 2
  val KAFKA_TOPIC = "logs-avro"
  val KAFKA_BROKERS = "localhost:9092"
  val SCHEMA_REGISTRY = "http://localhost:8081"


  val APP_NAME = Avro01LogConsole.getClass.getName
  LoggerUtil.disableSparkLogs()

  val session = SparkSession.builder
    .appName(APP_NAME)
    .master("local[*]")
    .enableHiveSupport.getOrCreate

  val ssc = new StreamingContext(session.sparkContext, Seconds(BATCH_DURATION))

  val kafkaDStream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, buildConsumerStrategy)

  var structType = SchemaUtil.buildStructTypeFromSchemaRegistry(SCHEMA_REGISTRY, KAFKA_TOPIC)
  var offsetRanges = Array.empty[OffsetRange]
  kafkaDStream.foreachRDD(message => {
    val rows = message.map(record => record.value.toString)
    val count = rows.count()
    if (count > 0) {
      println(s"\n${new Date()} : ############# CONSUMING NEXT BATCH ############# Count=$count")

      println("Partition offset range-")
      offsetRanges = message.asInstanceOf[HasOffsetRanges].offsetRanges.sortBy(_.partition)
      for (offsetRange <- offsetRanges) {
        println(s"${offsetRange.partition} : [${offsetRange.fromOffset} - ${offsetRange.untilOffset}]")
      }

      val df = session.sqlContext.read.schema(structType).json(rows)
      df.show()

      kafkaDStream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      println(s"${new Date()} : Offsets committed back to kafka")
    }
  })

  ssc.start()
  ssc.awaitTermination()


  private def buildConsumerStrategy: ConsumerStrategy[String, GenericData.Record] = {
    val KAFKA_PARAMS = Map[String, Object](
      "bootstrap.servers" -> KAFKA_BROKERS,
      "schema.registry.url" -> SCHEMA_REGISTRY,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[KafkaAvroDeserializer],
      "group.id" -> s"$APP_NAME-group-id",
      "enable.auto.commit" -> (false: java.lang.Boolean))

    val TOPIC_NAME = List(KAFKA_TOPIC)
    ConsumerStrategies.Subscribe(TOPIC_NAME, KAFKA_PARAMS)
  }
}


