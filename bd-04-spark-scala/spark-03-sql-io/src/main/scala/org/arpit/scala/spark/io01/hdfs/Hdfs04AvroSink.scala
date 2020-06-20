package org.arpit.scala.spark.io01.hdfs

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.io00.common.LoggerUtil

/**
 * Refer README.md for installing hadoop locally
 *
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 *
 * Create /data directory (if it doesn't exist) with permissions using following commands-
 * ~$ hdfs dfs -mkdir /data
 * ~$ hdfs dfs -chmod -R 1777 /data
 */
object Hdfs04AvroSink extends App {
  private val APP_NAME = Hdfs04AvroSink.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  dataset.printSchema()
  dataset.show(5)

  val hdfsPath = "hdfs://localhost:9000/data/employee-data/avro"
  dataset.coalesce(1)
    .write
    .mode("overwrite")
    .format("avro")
    .option("compression", "snappy")
    .save(hdfsPath)

  println("Data written in csv format at hdfs location : " + hdfsPath)
}
