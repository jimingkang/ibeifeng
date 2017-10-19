package com.ibeifeng.bigdata.offline.analystics.nu;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.ibeifeng.bigdata.offline.analystics.common.GlobalConstants;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.stats.StatsUserDimension;
import com.ibeifeng.bigdata.offline.analystics.util.HbaseScanUtil;

public class TestNewRunner implements Tool {
	
	Configuration conf = new Configuration();
	private HbaseScanUtil hbasescan = new HbaseScanUtil();

	@Override
	public void setConf(Configuration conf) {
		// TODO Auto-generated method stub
		// 本地运行
		conf.set("fs.defaultFS", "hdfs://hadoop-senior01.ibeifeng.com:8020");
		conf.set("yarn.resourcemanager.hostname", "hadoop-senior01.ibeifeng.com");
		conf.set("hbase.zookeeper.quorum", "hadoop-senior01.ibeifeng.com:2181");
		conf.set(GlobalConstants.RUNNING_DATE_PARAMES, "2015-12-20");
		//////

		// 加载资源文件
		// 输出器  给sql中？赋值的collector类名称
		conf.addResource("output-collector.xml");
		// 输出结果到mysql的执行sql更新语句
		conf.addResource("query-mapping.xml");
		// 数据库连接参数
		conf.addResource("transformer-env.xml");
		this.conf = HBaseConfiguration.create(conf);
	}

	@Override
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return this.conf;
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//job
		Job job = Job.getInstance(this.conf, "new-test");
		job.setJarByClass(TestNewRunner.class);
		//input && map
		List<Scan> scans = new ArrayList<Scan>();
		Scan scan = hbasescan.scanFilterUtil("20151220");
		scans.add(scan);
		TableMapReduceUtil.initTableMapperJob(scans, TestNewMapper.class, StatsUserDimension.class, 
				Text.class, job, false);
		
		//shuffle
		//reduce
		job.setReducerClass(TestNewReduce.class);
		job.setOutputKeyClass(StatsUserDimension.class);
		job.setOutputValueClass(LongWritable.class);
		//output
		Path outputDir = new Path("/test-project");
		FileOutputFormat.setOutputPath(job, outputDir);
		//submit
		
		return job.waitForCompletion(true)?0:1;
	}
	
	public static void main(String[] args) {
		try {
			int status = ToolRunner.run(new TestNewRunner(), args);
			System.exit(status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
