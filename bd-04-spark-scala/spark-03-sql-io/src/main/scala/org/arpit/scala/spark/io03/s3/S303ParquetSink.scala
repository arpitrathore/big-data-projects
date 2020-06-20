package org.arpit.scala.spark.io03.s3

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.io00.common.LoggerUtil

/**
 * Refer README.md for installing minio-s3 locally
 *
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 * Create bucket using following command
 * $ s3cmd mb s3://log-data
 */
object S303ParquetSink extends App {
  private val APP_NAME = S302OrcSink.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .config("fs.s3a.access.key", "myaccesskey9005")
    .config("fs.s3a.secret.key", "mysecretkey9005")
    .config("fs.s3a.endpoint", "http://127.0.0.1:9005")
    .config("spark.hadoop.fs.s3a.path.style.access", true)
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  dataset.printSchema()
  dataset.show(5)

  val s3Path = "s3a://log-data/parquet"
  dataset.coalesce(1)
    .write
    .mode("overwrite")
    .format("parquet")
    .option("compression", "snappy")
    .save(s3Path)

  println("Data written in csv format at s3 location : " + s3Path)
}
