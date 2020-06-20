package org.arpit.scala.spark.structured00.common

import java.net.URL

import com.fasterxml.jackson.databind.node.{ArrayNode, TextNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.spark.sql.types.{DataType, DataTypes, StructType}

object SchemaUtil {

  def buildStructTypeFromSchemaRegistry(schemaRegistry: String, kafkaTopic: String): StructType = {
    val avroSchemaUrl = schemaRegistry + "/subjects/" + kafkaTopic + "-value/versions/latest/schema"
    println(s"Schema url : $avroSchemaUrl")
    val schema = new ObjectMapper().readTree(new URL(avroSchemaUrl)).path("fields").asInstanceOf[ArrayNode]
    buildStructType(schema)
  }

  private def buildStructType(schema: ArrayNode) = {
    var structType = new StructType
    import scala.collection.JavaConversions._
    for (jsonNode <- schema) {
      val logicalType = jsonNode.findValue("logicalType")
      val typeNode = jsonNode.findValue("type")
      var fieldType = DataTypes.StringType
      if (typeNode.isInstanceOf[TextNode]) fieldType = getStructFieldDataType(typeNode.asText, logicalType)
      else if (typeNode.isInstanceOf[ArrayNode]) if (typeNode.size == 2 && typeNode.get(0).isTextual && typeNode.get(1).isTextual) if (typeNode.get(0).asText == "null") fieldType = getStructFieldDataType(typeNode.get(1).asText, logicalType)
      else if (typeNode.get(1).asText == "null") fieldType = getStructFieldDataType(typeNode.get(0).asText, logicalType)
      val name = jsonNode.findValue("name").asText
      structType = structType.add(name, fieldType)
    }
    structType
  }

  private def getStructFieldDataType(datatype: String, logicalType: JsonNode): DataType = datatype match {
    case "long" =>
      if (logicalType != null && (logicalType.asText == "timestamp-millis" || logicalType.asText == "timestamp-micros")) {
        DataTypes.TimestampType
      } else {
        DataTypes.LongType
      }
    case "int" =>
      DataTypes.IntegerType
    case "boolean" =>
      DataTypes.BooleanType
    case "float" =>
      DataTypes.FloatType
    case "double" =>
      DataTypes.DoubleType
    case "bytes" =>
      DataTypes.BinaryType
    case _ =>
      DataTypes.StringType
  }

}
