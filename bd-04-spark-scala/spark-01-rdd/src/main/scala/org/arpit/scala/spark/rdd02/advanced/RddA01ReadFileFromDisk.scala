package org.arpit.scala.spark.rdd02.advanced

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object RddA01ReadFileFromDisk extends App {
  val APP_NAME = RddA01ReadFileFromDisk.getClass.getName
  LoggerUtil.disableSparkLogs

  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  val rdd = sc.textFile("bd-03-spark-scala/spark-01-rdd/src/main/resources/spark-logs.txt")

  println("############## Top 10 lines from the file ##############\n")
  rdd.take(10).foreach(line => println(line))

}
