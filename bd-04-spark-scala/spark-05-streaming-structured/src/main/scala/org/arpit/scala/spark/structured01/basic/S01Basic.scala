package org.arpit.scala.spark.structured01.basic

import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{DataTypes, StructType}
import org.arpit.scala.spark.structured00.common.{LoggerUtil, SchemaUtil}

object S01Basic extends App {

  val APP_NAME = S01Basic.getClass.getName
  LoggerUtil.disableSparkLogs()

  val BATCH_DURATION = 2
  val KAFKA_TOPIC = "logs-avro"
  val KAFKA_BROKERS = "localhost:9092"
  val SCHEMA_REGISTRY = "http://localhost:8081"


  val spark = SparkSession.builder.appName(APP_NAME).master("local[*]").getOrCreate

  val dataset = spark.readStream.format("kafka")
    .option("kafka.bootstrap.servers", KAFKA_BROKERS)
    .option("subscribe", KAFKA_TOPIC)
    .option("startingoffsets", "earliest")
    .load()

  val structType = SchemaUtil.buildStructTypeFromSchemaRegistry(SCHEMA_REGISTRY, KAFKA_TOPIC)

  val withNonFlattenedSchema = dataset.select(from_json(col("value").cast(DataTypes.StringType), structType).as("logs"))
  withNonFlattenedSchema.printSchema()

  val withFlattenedSchema = withNonFlattenedSchema.select("logs.*")
  withFlattenedSchema.printSchema()

  withFlattenedSchema
    .writeStream
    .format("console")
    .option("truncate", false)
    .outputMode(OutputMode.Append)
    .start
    .awaitTermination()
}
