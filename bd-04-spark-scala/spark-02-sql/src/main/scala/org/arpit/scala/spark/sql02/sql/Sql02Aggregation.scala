package org.arpit.scala.spark.sql02.sql

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.sql00.common.LoggerUtil

/**
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 */
object Sql02Aggregation extends App {
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

  println("employeesByCity")
  val employeesByCity = spark.sql("select city, count(1) as city_count from employee group by city order by city_count desc")
  employeesByCity.printSchema()
  employeesByCity.show(5)

  println("averageSalaryByCity")
  val averageSalaryByCity = spark.sql("select city, avg(salary) as avg_sal from employee group by city order by avg_sal desc")
  averageSalaryByCity.show(5)

  println("averageSalaryByCityAndJobTitle")
  val averageSalaryByCityAndJobTitle = spark.sql("select city, jobTitle, avg(salary) as avg_sal from employee group by city, jobTitle order by avg_sal desc limit 5")
  averageSalaryByCityAndJobTitle.show(10, false)
}
