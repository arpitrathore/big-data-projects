package org.arpit.scala.spark.sql01.api

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.sql00.common.LoggerUtil

import org.apache.spark.sql.functions._

object Api00InMemoryDataSet extends App {

  private val APP_NAME = Api00InMemoryDataSet.getClass.getName

  LoggerUtil.disableSparkLogs()


  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val df = spark.createDataFrame(Seq(Product(1, "Nescafe", System.currentTimeMillis())))

  df.show()


  val withDateCol = df.withColumn("mfd_ts", from_unixtime(col("mfd").divide(1000)))

  withDateCol.show()

  val gg = withDateCol.withColumn("year", year(col("mfd_ts"))).withColumn("day", dayofyear(col("mfd_ts"))).withColumn("hour", hour(col("mfd_ts"))).drop(col("mfd_ts"))
  gg.show()
}

case class Product(id: Int, name: String, mfd: Long)
