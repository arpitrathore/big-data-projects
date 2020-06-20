package org.arpit.scala.spark.sql02.sql

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.sql00.common.LoggerUtil

/**
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 */
object Sql01ShowAll extends App {
  private val APP_NAME = Sql02Aggregation.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  dataset.createOrReplaceTempView("employee")

  println("allEmployees")
  val allEmployees = spark.sql("select * from employee limit 5")
  allEmployees.printSchema()
  allEmployees.show()
}
