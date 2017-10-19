package com.ibeifeng.bigdata.spark.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 *
 *  此函数mapWithState 在Spark 1.6中出现，也是实时状态更新，性能要比updateStateByKey好很多
 *  作为作业：大家完成
 *      参考；https://github.com/apache/spark/blob/master/examples/src/main/scala/org/apache/spark/examples/streaming/StatefulNetworkWordCount.scala
 *
 */
object UpdateStateByKeyKafkaWordCountStreamingHA {

   val checkpointDirectory = "/user/beifeng/spark/streaming/chkpoint-11111"

   def main(args: Array[String]): Unit = {

     // Function to create and setup a new StreamingContext
     def functionToCreateContext(): StreamingContext = {
       /**
        * Step 1: Create StreamingContext
        */
       val sparkConf = new SparkConf()
         .setAppName("WordCountStreaming Application HA")
         .setMaster("local[2]")  // 通过参数传递

       val sc = SparkContext.getOrCreate(sparkConf)

       // Seconds(5)代表的是 每次处理多长时间范围内的数据
       // 批次时间间隔:batch interval
       val ssc = new StreamingContext(sc, Seconds(5))

       /**
        * Step 2: Create DStream and DStream#Transformation
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

       val words = lines.flatMap(_.split(" "))
       // Count each word in each batch
       val pairs = words.map(word => (word, 1))
       val wordCounts: DStream[(String, Int)] = pairs.updateStateByKey(
         (values: Seq[Int], state: Option[Int]) => {
           // 获取当前Key传递进行的值
           val currentCount = values.sum

           // 获取Key以前状态中的值
           val previousCount = state.getOrElse(0)

           // update state and return
           Some(currentCount + previousCount)
         }
       )

       // Print the first ten elements of each RDD generated in this DStream to the console
       wordCounts.print()

       /**
        * 设置检查点目录并返回StreamingContext，便于启动
        *
        *   下面的检查点目录很重要，一般情况设置为HDFS的目录
        */
       ssc.checkpoint(checkpointDirectory)   // set checkpoint directory
       ssc
     }

     // Get StreamingContext from checkpoint data or create a new one
     val context = StreamingContext.getOrCreate(checkpointDirectory,
        functionToCreateContext _)

     // Start the context
     context.start()
     context.awaitTermination()
   }

 }
