package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.{Employee, LoggerUtil}

object Rdd06PairRDDGroupBykey extends App {
  val APP_NAME = Rdd06PairRDDGroupBykey.getClass.getName
  LoggerUtil.disableSparkLogs

  val inputData = Employee.buildRandomEmployees(50)
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  println("######## DO NOT USE grpoupByKey() for large data ########")
  println("Unsorted employees groupBy city using very inefficient grpoupByKey()")
  println("\nEmployee groupBy city-\n")

  sc.parallelize(inputData)
    .map(e => (e.city, 1))
    .groupByKey()
    .foreach(tuple => println(tuple._1 + " => " + tuple._2.size))


}
