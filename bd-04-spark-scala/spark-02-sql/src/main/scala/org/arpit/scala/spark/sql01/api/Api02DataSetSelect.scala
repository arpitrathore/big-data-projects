package org.arpit.scala.spark.sql01.api

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.arpit.scala.spark.sql00.common.LoggerUtil

/**
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 */
object Api02DataSetSelect extends App {

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

  // Job role with highest salaries
  val highestSalaryRoles = dataset
    .select(col("salary"), col("jobTitle"), col("city"))
    .orderBy(col("salary").desc)
  highestSalaryRoles.show(5, false)

  // Job role with lowest salaries
  val lowestSalaryRoles = dataset
    .select(col("salary"), col("jobTitle"), col("city"))
    .orderBy(col("salary"))
  lowestSalaryRoles.show(5, false)
}
