package org.arpit.scala.spark.io04.cassandra

import org.apache.spark.sql.SparkSession
import org.arpit.scala.spark.io00.common.LoggerUtil


/**
 * Refer README.md for installing cassandra locally
 *
 * Run bd-02-python-data/01-csv/01-employee-csv-generator.py to generate csv file
 *
 * Start cassandra and start cqlsh
 * cqlsh> CREATE KEYSPACE spark_ks WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
 * cqlsh> use spark_ks;
 * cqlsh:spark_ks> CREATE TABLE employee(id,firstName,lastName,gender,doj,age,email,mobile,salary,companyName,jobTitle,city,country, PRIMARY KEY("ip", "date"));
 */
object Cass01Sink extends App {

  private val APP_NAME = Cass01Sink.getClass.getName
  LoggerUtil.disableSparkLogs()

  val spark = SparkSession.builder()
    .appName(APP_NAME)
    .master("local[*]")
    .getOrCreate

  val dataset = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv("bd-03-test-data/employees.csv")

  dataset.printSchema()
  dataset.show(5)

  val keyspace = "spark_ks"
  val table = "logs"

  dataset.write
    .format("org.apache.spark.sql.cassandra")
    .mode("overwrite")
    .option("confirm.truncate", "true")
    .option("spark.cassandra.connection.host", "localhost")
    .option("spark.cassandra.connection.port", "9042")
    .option("keyspace", keyspace)
    .option("table", table)
    .save()

  println(s"Written data to cassandra successfully. Keypspace=$keyspace, table=$table. Row count${dataset.count}")
}
