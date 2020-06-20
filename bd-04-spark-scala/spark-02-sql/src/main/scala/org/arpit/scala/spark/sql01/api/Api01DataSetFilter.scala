package org.arpit.scala.spark.sql01.api

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Row, SparkSession}
import org.arpit.scala.spark.sql00.common.LoggerUtil

/**
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 */
object Api01DataSetFilter extends App {

  private val APP_NAME = Api03DataSetAggregation.getClass.getName

  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  //Show all
  dataset.printSchema
  dataset.show(5, false)

  // Filter using string expression
  val youngFemaleEmployees = dataset.filter("gender='Female' AND age < 30")
  System.out.println("Count of young female employees : " + youngFemaleEmployees.count)
  youngFemaleEmployees.show(5, false)

  // Filter using lambda expression
  val gmailEmployees = dataset.filter((row: Row) => row.getAs("email").toString.endsWith("@gmail.com"))
  System.out.println("Count of gmail employees : " + gmailEmployees.count)
  gmailEmployees.show(5, false)

  // Filter using column expression
  val mumbaiManagerEmployees = dataset.filter(col("city").equalTo("Mumbai").and(col("jobTitle").like("%Manager%")))
  System.out.println("Count of Mumbai managers : " + mumbaiManagerEmployees.count)
  mumbaiManagerEmployees.show(5, false)
}
