package com.cnx.kafka
import kafka.serializer.DefaultDecoder
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.sql.DataFrame
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.sql.catalyst.expressions.Second
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.kafka.`package`
import org.apache.spark.storage.StorageLevel
import breeze.storage.Storage
import org.apache.spark.api.java.StorageLevels
import org.apache.spark.sql.DataFrame

import org.apache.spark.streaming.scheduler.StreamingListener

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils

//import  org.apache.kafka.common.message. //kafka.serializer.StringDecoder

/**
 * fetch data from Apache kafka service
 * 
 */
class DataReceivers(sc: SparkContext) {
  
  /**
   * access the kakfa data service
   */
  def receiveDataFromKafka(topic:String):DataFrame = {
    //create spark streaming..
    val ssc = new StreamingContext(sc, Seconds(1))
    val mytopic = topic.map { x => x }
    //val kafkaParams = Map("metadata.broker.list" -> "172.17.42.1:9092", "metadata.broker.list" -> "172.17.42.1:9092")
    //val kafkaStream = KafkaUtils.createStream(ssc, kafkaParams, topic, StorageLevels.MEMORY_AND_DISK)
    
    
    return null;
  }
  
  def directlyDataFromKafka(topic:String): DataFrame={
    
    //create spark streaming..
    val ssc = new StreamingContext(sc, Seconds(2))
    // Define the Kafka parameters, broker list must be specified
    val kafkaParams = Map[String, String]("metadata.broker.list" -> "192.168.1.106:9092")
        
        //"group.id" -> "0"
 
    // Define which topics to read from
    val topics = Set(topic)
    ssc.checkpoint("/tmp")
    ssc.addStreamingListener(new RuleFileListenerB())
    // Create the direct stream with the Kafka parameters and topics
    //val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
  
    val kafkaStream = KafkaUtils.createDirectStream[Array[Byte], Array[Byte], DefaultDecoder, DefaultDecoder](ssc, kafkaParams, topics)
  
    // Define the offset ranges to read in the batch job
    val offsetRanges = Array(
      OffsetRange(topic, 0, 10, 200000))
      // Create the RDD based on the offset ranges
     val rdd = KafkaUtils.createRDD[String, String, StringDecoder, StringDecoder](sc, kafkaParams, offsetRanges)
 
     var i=0
     rdd.foreachPartition { 
      
      partitionOfRecords =>
       partitionOfRecords.foreach {i+=1; message => System.out.println(i + "====="+message.toString())}}
    
    ssc.start()
    ssc.awaitTermination()
    
     return null
  }
}

class RuleFileListenerB extends StreamingListener {

  override def onBatchStarted(batchStarted : org.apache.spark.streaming.scheduler.StreamingListenerBatchStarted) {
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
        println("check whether the file's modified date is change, if change then reload the configuration file")
    //val source = scala.io.Source.fromFile("D:/code/scala/test")
    //val lines = try source.mkString finally source.close()
    //println(lines)
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
  }

}