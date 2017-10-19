package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CalRunner {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop.ibeifeng.com:8020");
		conf.setDouble("pagerank.q.value", 0.85);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(CalRunner.class);
		
		job.setMapperClass(CalDataMap.class);
		job.setReducerClass(CalDataRudece.class);
		job.setOutputKeyClass(CalOutputkey.class);
		job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(CalOutputkey.class);
        job.setMapOutputValueClass(Text.class);
		
		//input ,output
		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path outputDir = new Path(args[1]);
		FileSystem.get(conf).delete(outputDir, true);
		FileOutputFormat.setOutputPath(job, outputDir);
		job.waitForCompletion(true);
	}

}
