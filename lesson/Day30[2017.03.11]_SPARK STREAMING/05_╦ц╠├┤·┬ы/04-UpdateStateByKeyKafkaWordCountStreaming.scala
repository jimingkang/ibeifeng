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
object UpdateStateByKeyKafkaWordCountStreaming {

   def main(args: Array[String]) {
     val sparkConf = new SparkConf()
       .setAppName("WordCountStreaming Application")
       .setMaster("local[2]")  // 通过参数传递

     val sc = SparkContext.getOrCreate(sparkConf)

     // Seconds(5)代表的是 每次处理多长时间范围内的数据
     // 批次时间间隔:batch interval
     val ssc = new StreamingContext(sc, Seconds(5))

     /**
      * 设置检查点，用于存储 updateStateByKey中Key的状态信息，存储到文件中
      */
     ssc.checkpoint(".")

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


     /**
      * 此处使用updateStateByKey ，来更新以前的State, 并保存起来
      *     依据函数的名称，可以看出，DStream的类型必须是二元组
      *     功能：
      *         依据Key去更新Value的值（状态)
      *   def updateStateByKey[S: ClassTag](
            updateFunc: (Seq[V], Option[S]) => Option[S]
          ): DStream[(K, S)]

          DStream[(K, V)]
          updateFunc: (Seq[V], Option[S]) => Option[S]
            Seq[V]:
                V：代表的是DStream中Value的类型,针对WordCount程序来说，V就是Int
                代表的是当前批次Key的所有Value的集合Seq
            Option[S]：
                S：代表的状态State，存储的是以前Key的分析结果，针对WordCount程序来说，S就是Count， Int
                注意一点：
                    S 可以是任意的数据类型， 依据实际需求而定
                Option\Some有值\None无值
      */
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

     ssc.start()             // Start the computation
     ssc.awaitTermination()  // Wait for the computation to terminate

     // SparkStreaming Stop
     ssc.stop()    // 包含  sc.stop()
   }

 }
