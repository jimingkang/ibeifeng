
##  Usage: spark-submit [options] <app jar | python file> [app arguments]

SPARK_HOME=/opt/cdh-5.3.6/spark-1.6.1-bin-2.5.0-cdh5.3.6

SPARK_APP_JARS_DIR=/opt/cdh-5.3.6/spark-1.6.1-bin-2.5.0-cdh5.3.6/appJars

${SPARK_HOME}/bin/spark-submit \
--master spark://hadoop-senior02.ibeifeng.com:7077 \
--deploy-mode cluster \
--class com.ibeifeng.bigdata.spark.core.LogAnalyzerSpark \
--driver-memory 1G \
--executor-memory 2G \
--executor-cores 2 \
--total-executor-cores 8 \
${SPARK_APP_JARS_DIR}/logs-analyzer-1.0.jar



${SPARK_HOME}/bin/spark-submit \
--master yarn \
--deploy-mode cluster \
--class com.ibeifeng.bigdata.spark.core.LogAnalyzerSpark \
--driver-memory 1G \
--executor-memory 2G \
--executor-cores 2 \
--num-executors 4 \
${SPARK_APP_JARS_DIR}/logs-analyzer-1.0.jar

