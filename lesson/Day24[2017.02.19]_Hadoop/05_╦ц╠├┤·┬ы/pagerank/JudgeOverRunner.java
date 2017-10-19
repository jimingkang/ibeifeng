package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class JudgeOverRunner {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop.ibeifeng.com:8020");
		conf.setDouble("limit", 0.00001);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(JudgeOverRunner.class);
		
		job.setMapperClass(JudgeOverMap.class);
		job.setReducerClass(JudgeOverReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setPartitionerClass(part.class);
        job.setGroupingComparatorClass(group.class);
        job.setNumReduceTasks(1);
		
		//input ,output
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileInputFormat.addInputPath(job, new Path(args[1]));
		Path outputDir = new Path(args[2]);
		FileSystem.get(conf).delete(outputDir, true);
		FileOutputFormat.setOutputPath(job, outputDir);
		job.waitForCompletion(true);
	}

	public static class part extends Partitioner<Text, Text>{

		@Override
		public int getPartition(Text key, Text value, int numPartitions) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	public static class group extends WritableComparator{
		public group() {
			// TODO Auto-generated constructor stub
			super(Text.class,true);
		}
		
		@Override
		public int compare(WritableComparable a, WritableComparable b) {
			// TODO Auto-generated method stub
			//不管任何参数都相等
			return 0;		}
	}
}
