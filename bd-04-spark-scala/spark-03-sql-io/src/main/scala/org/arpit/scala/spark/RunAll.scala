package org.arpit.scala.spark

import org.arpit.scala.spark.io03.s3.{S301CsvSink, S302OrcSink, S303ParquetSink, S304AvroSink}

object RunAll extends App {

  S301CsvSink.main(args)
  S302OrcSink.main(args)
  S303ParquetSink.main(args)
  S304AvroSink.main(args)
}
