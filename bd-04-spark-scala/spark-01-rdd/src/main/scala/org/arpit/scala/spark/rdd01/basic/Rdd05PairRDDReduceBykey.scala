package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.{Employee, LoggerUtil}

object Rdd05PairRDDReduceBykey extends App {
  val APP_NAME = Rdd04Tuple.getClass.getName

  LoggerUtil.disableSparkLogs
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val inputData = Employee.buildRandomEmployees(200)
  val employeeRdd = sc.parallelize(inputData)

  val employeesCountByAge = employeeRdd.map(e => (e.age, 1)).reduceByKey(_ + _)
  val employeeCountByAgeSorted = employeesCountByAge.map(_.swap).sortByKey(false)

  println("Employee count by age:")
  employeeCountByAgeSorted.take(5).map(_.swap).foreach(println)
}
