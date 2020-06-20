package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object Rdd04Tuple extends App {
  val APP_NAME = Rdd04Tuple.getClass.getName

  LoggerUtil.disableSparkLogs
  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val inputData = List(25, 43, 20, 77, 88, 99, 144, 196, 65536);
  val inteterRdd = sc.parallelize(inputData)

  val intSqrtTuple = inteterRdd.map(i => (i, Math.sqrt(i)));

  intSqrtTuple.foreach(println)
}
