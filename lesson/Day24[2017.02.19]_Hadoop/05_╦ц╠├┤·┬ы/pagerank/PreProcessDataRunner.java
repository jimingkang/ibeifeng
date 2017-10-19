package org.apache.hadoop.pagerank;

import java.io.IOException;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PreProcessDataRunner {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop.ibeifeng.com:8020");
		conf.setInt("first.PR.value", 1);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(PreProcessDataRunner.class);
		
		job.setMapperClass(PreProcessDataFormatMap.class);
		job.setReducerClass(PreProcessDataFormatReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
		
		//input ,output
		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path outputDir = new Path(args[1]);
		FileSystem.get(conf).delete(outputDir, true);
		FileOutputFormat.setOutputPath(job, outputDir);
		job.waitForCompletion(true);
		
	}
}
