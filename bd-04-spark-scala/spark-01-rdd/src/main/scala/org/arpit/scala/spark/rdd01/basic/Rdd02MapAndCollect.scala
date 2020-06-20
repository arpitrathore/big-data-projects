package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object Rdd02MapAndCollect extends App {
  val APP_NAME = Rdd02MapAndCollect.getClass.getName

  LoggerUtil.disableSparkLogs
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val inputData = List(25, 43, 20, 77, 88, 99)
  val result = sc.parallelize(inputData).map(Math.sqrt(_))

  val resultList = result.collect()
  resultList.foreach(println)
}
