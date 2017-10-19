package com.ibeifeng.bigdata.offline.analystics.nu;

import java.io.IOException;

import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class DemoOutput<K, V> extends OutputFormat<K, V> {

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
		//返回真正的输出器
	}

	@Override
	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		//检查输出空间，如果输出空间异常，直接报异常
	}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
		//返回一个job相关属性的对象，一般直接参考DBoutputformat中的写法，直接返回fileoutputcommitter
		//  return new FileOutputCommitter(FileOutputFormat.getOutputPath(context),context);
	}
	
	public static class demoRecordwriter<K, V> extends RecordWriter<K, V>{

		@Override
		public void write(K key, V value) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			//真正将key和value写出的方法
			
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			//关闭资源的方法
		}
		
	}

}
