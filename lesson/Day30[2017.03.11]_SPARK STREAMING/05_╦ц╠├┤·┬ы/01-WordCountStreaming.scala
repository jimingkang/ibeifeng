package com.ibeifeng.bigdata.spark.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by XuanYu on 2017/3/12.
 */
object WordCountStreaming {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("WordCountStreaming Application")
      .setMaster(args(0))  // 通过参数传递

    val sc = SparkContext.getOrCreate(sparkConf)

    // Seconds(5)代表的是 每次处理多长时间范围内的数据
    // 批次时间间隔:batch interval
    val ssc = new StreamingContext(sc, Seconds(5))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val lines = ssc.socketTextStream("hadoop-senior01.ibeifeng.com", 9999)

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
