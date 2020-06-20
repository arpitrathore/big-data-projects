package org.arpit.scala.spark.sql01.api

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{avg, col, date_format}
import org.apache.spark.sql.types.DataTypes
import org.arpit.scala.spark.sql00.common.LoggerUtil

/**
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 */
object Api03DataSetAggregation extends App {

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

  val employeesByJobTitle = dataset
    .groupBy(col("jobTitle"))
    .count
    .orderBy(col("count").desc)

  //Same as below
  /*employeesByJobTitle = dataset.groupBy(col("jobTitle")).agg(count(col("*")).alias("job_count")).orderBy(col("job_count").desc());*/
  employeesByJobTitle.show(5, false)

  var employeesByYoj = dataset
    .select(col("firstName"),
      date_format(col("doj").divide(1000).cast(DataTypes.TimestampType), "yyyy").alias("yoj"),
      col("city"))
    .orderBy(col("yoj"))
  employeesByYoj.show(5);

  employeesByYoj = employeesByYoj
    .groupBy(col("yoj"))
    .count
    .orderBy(col("count").desc)
  employeesByYoj.show(5)

  val averageSalaryByCity = dataset
    .groupBy(col("city"))
    .agg(avg(col("salary")).alias("avg_salary"))
    .orderBy(col("avg_salary").desc)
  averageSalaryByCity.show(5)
}
