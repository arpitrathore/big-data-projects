package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object Rdd03MapReduceAndCount extends App {
  val APP_NAME = Rdd03MapReduceAndCount.getClass.getName

  LoggerUtil.disableSparkLogs
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val inputData = List(25, 43, 20, 77, 88, 99, 33, 99, 55)
  val rdd = sc.parallelize(inputData)

  val count = rdd.map(_ => 1L).reduce((v1: Long, v2: Long) => v1 + v2)
  println("RDD count : " + count)
  println("RDD count with rdd.count() method " + rdd.count)
}
