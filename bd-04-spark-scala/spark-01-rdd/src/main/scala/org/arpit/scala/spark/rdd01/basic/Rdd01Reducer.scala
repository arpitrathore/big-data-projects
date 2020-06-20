package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object Rdd01Reducer extends App {
  val APP_NAME = Rdd01Reducer.getClass.getName

  LoggerUtil.disableSparkLogs
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val inputList = List(25.5, 43.45, 20.44, 77.77, 88.99, 99.33)
  val rdd = sc.parallelize(inputList)

  val result = rdd.reduce((v1: Double, v2: Double) => v1 + v2)
  System.out.println(result)
}
