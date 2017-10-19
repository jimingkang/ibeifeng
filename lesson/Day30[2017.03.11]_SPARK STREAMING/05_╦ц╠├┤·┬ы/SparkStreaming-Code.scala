
import org.apache.spark._
import org.apache.spark.streaming._

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



def createDirectStream[
    K: ClassTag,
    V: ClassTag,
    KD <: Decoder[K]: ClassTag,
    VD <: Decoder[V]: ClassTag] (
      ssc: StreamingContext,
      kafkaParams: Map[String, String],
      topics: Set[String]
  ): InputDStream[(K, V)]



def reduceByKeyAndWindow(
	reduceFunc: (V, V) => V,
	windowDuration: Duration,
	slideDuration: Duration
): DStream[(K, V)] 