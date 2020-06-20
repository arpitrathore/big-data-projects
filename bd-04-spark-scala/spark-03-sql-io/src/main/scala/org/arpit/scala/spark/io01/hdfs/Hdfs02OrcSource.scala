package org.arpit.scala.spark.io01.hdfs

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.io00.common.LoggerUtil

/**
 * Run [[Hdfs01CsvSink]] class to generate ORC files in hdfs
 */
object Hdfs02OrcSource extends App {
  private val APP_NAME = Hdfs02OrcSource.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .orc("hdfs://localhost:9000/arpit-logs-partitioned/year=2020/day=125")

  dataset.printSchema()
  dataset.show(5, false)
}
