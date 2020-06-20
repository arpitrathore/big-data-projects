package org.arpit.scala.spark.io05.elastic

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.io00.common.LoggerUtil


/**
 * Refer README.md for installing elasticsearch locally
 *
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 *
 * Start elasticsearch server
 */
object Es01Sink extends App {

  private val APP_NAME = Es01Sink.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .config("spark.es.nodes", "localhost")
    .config("spark.es.port", "9200")
    .config("es.index.auto.create", "true")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  dataset.printSchema()
  dataset.show(5)

  val esIndex = "logs"

  import org.elasticsearch.spark.sql._

  dataset.toDF.saveToEs(s"$esIndex")

  println(s"Written data to ES successfully to index=$esIndex. Row count${dataset.count}")
}
