package org.arpit.scala.spark.rdd01.basic

import org.apache.spark.{SparkConf, SparkContext}
import org.arpit.scala.spark.rdd00.common.LoggerUtil

object Rdd07FlatMap extends App {

  val APP_NAME = Rdd07FlatMap.getClass.getName
  LoggerUtil.disableSparkLogs

  val inputData = List("Hi! My name is arpit",
    "",
    "I am from pune",
    "My favourite programming language is Java",
    "",
    "But I also like scala!!")

  val conf = new SparkConf().setAppName(APP_NAME).setMaster("local[*]")
  val sc = new SparkContext(conf)

  sc.parallelize(inputData).flatMap(line => line.split(" "))
    .filter(word => !word.isEmpty)
    .map(word => (word, 1))
    .reduceByKey(_ + _)
    .foreach(word => println(word))

}
