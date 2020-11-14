package org.arpit.scala.flink.ds01.basic

import org.apache.flink.api.scala._

object DS01WordCount extends App {

  val env = ExecutionEnvironment.getExecutionEnvironment
  val text = env.fromElements(
    "Who is there?",
    "I think I hear them. Stand, ho! Who is there?")

  val counts = text
    .flatMap(line => line.toLowerCase.split("\\W+"))
    .filter(word => word.nonEmpty)
    .map((_, 1))
    .groupBy(0)
    .sum(1)

  counts.print()
}
