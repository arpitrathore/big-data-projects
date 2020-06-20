package org.arpit.scala.spark.rdd00.common

import org.apache.log4j.{Level, Logger}

object LoggerUtil {

  def disableSparkLogs(): Unit = {
    Logger.getLogger("org.apache").setLevel(Level.WARN)
    Logger.getLogger("org.apache.spark.storage").setLevel(Level.ERROR)
  }
}
