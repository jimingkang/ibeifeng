package com.ibeifeng.bigdata.spark.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by XuanYu on 2017/3/12.
  */
object KafkaWordCountStreaming {

   def main(args: Array[String]) {
     val sparkConf = new SparkConf()
       .setAppName("WordCountStreaming Application")
       .setMaster("local[2]")  // 通过参数传递

     val sc = SparkContext.getOrCreate(sparkConf)

     // Seconds(5)代表的是 每次处理多长时间范围内的数据
     // 批次时间间隔:batch interval
     val ssc = new StreamingContext(sc, Seconds(5))

     // Create a DStream that will connect to hostname:port, like localhost:9999
     /**
      * def createDirectStream[
          K: ClassTag,
          V: ClassTag,
          KD <: Decoder[K]: ClassTag,
          VD <: Decoder[V]: ClassTag] (
            ssc: StringDecoder,
            kafkaParams: Map[String, String],
            topics: Set[String]
        ): InputDStream[(K, V)]
      */

     // Kafka Cluster
     val kafkaParams: Map[String, String] = Map("metadata.broker.list" ->
        "hadoop-senior03.ibeifeng.com:9092,hadoop-senior04.ibeifeng.com:9092,hadoop-senior05.ibeifeng.com:9092")

     // Kafka Topic
     val topics: Set[String] = Set("sparkTopic")

     // 从kafka Topic中读取数据，采用拉取的方式
     val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, // StringDecoder
       kafkaParams ,
       topics).map(tuple => tuple._2)  // 仅仅获取value的值即可

     // Split each line into words
     val words = lines.flatMap(_.split(" "))
     // Count each word in each batch
     val pairs = words.map(word => (word, 1))
     val wordCounts = pairs.reduceByKey(_ + _)

     // Print the first ten elements of each RDD generated in this DStream to the console
     wordCounts.print()

     ssc.start()             // Start the computation
     ssc.awaitTermination()  // Wait for the computation to terminate

     // SparkStreaming Stop
     ssc.stop()    // 包含  sc.stop()
   }

 }
