
// SparkSQL 提供异构数据源之间的访问的解决方案
// sqlContext.read.........

// /datas/resources

// 1、读取JSON格式的数据
val json_df = sqlContext.read.json("/datas/resources/people.json")
// json_df: org.apache.spark.sql.DataFrame = [age: bigint, name: string]
json_df.show

json_df.write.save("/datas/resources/json-result")

// 2、读取parquet格式的数据 	users.parquet
val parquet_df = sqlContext.read.parquet("/datas/resources/users.parquet")
// parquet_df: org.apache.spark.sql.DataFrame = [name: string, favorite_color: string, favorite_numbers: array<int>]

val user_df = sqlContext.read.load("/datas/resources/users.parquet")
// user_df: org.apache.spark.sql.DataFrame = [name: string, favorite_color: string, favorite_numbers: array<int>]

// 上述反应出，SparkSQL中默认的情况下加载的数据格式为parquet

// 3、读取text文件格式的数据
sqlContext.read.text("/datas/resources/people.txt").show
// res15: org.apache.spark.sql.DataFrame = [value: string]
// 感觉上述的功能与sc.textFile()


// =====================================================
/**
	从RDBMS的数据库中读写表的数据
 	read
def jdbc(url: String, table: String, properties: Properties): DataFrame
 	write
def jdbc(url: String, table: String, connectionProperties: Properties): Unit 	
 */
// 需求：
//		将Hive表中的数据与MySQL表中的数据进行关联分析
// 1、将Hive表中的dept表的数据写入到MySQL表中
val props = new java.util.Properties()
props.put("user", "root")
props.put("password", "123456")
sqlContext.read.table("db_hive.dept").write.jdbc("jdbc:mysql://hadoop-senior01.ibeifeng.com:3306/test", "tb_dept", props)

// SELECT e.ename, e.sal, d.dname FROM db_hive.emp e JOIN db_hive.dept d ON e.deptno = d.deptno ;

val hive_emp_df = sqlContext.read.table("db_hive.emp")
val mysql_dept_df = sqlContext.read.jdbc("jdbc:mysql://hadoop-senior01.ibeifeng.com:3306/test", "tb_dept", props)
hive_emp_df.join(mysql_dept_df, "deptno").select($"ename", $"sal", $"dname").show


sqlContext
	.read
	.table("db_hive.emp")
	.join(
		sqlContext
		.read
		.jdbc("jdbc:mysql://hadoop-senior01.ibeifeng.com:3306/test", "tb_dept", props), "deptno")
	.select($"ename", $"sal", $"dname")
	.show


/**
 通过自定义schema的方式将RDD装换为DataFrame
 */
val rdd = sc.textFile("/datas/resources/people.txt")

import org.apache.spark.sql._
val rowRdd = rdd.map(line => {
  val arr = line.split(",")
  Row(arr(0), arr(1).trim().toInt)
})

import org.apache.spark.sql.types._
val schema = StructType(
  Array(
    StructField("name", StringType, true),
    StructField("age", IntegerType, true)
  )
)

val people_df = sqlContext.createDataFrame(rowRdd, schema)