package com.ibeifeng.bigdata.offline.analystics.nu;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
/**
 * 
 * @author beifeng
 *
 * @param <K> : 输入的key
 * @param <V> ：输入的value
 */

public class DemoInputform<K, V> extends InputFormat<K, V> {

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
		/**
		 * f返回的 是一个分片的集合，用于分片
		 * 一个分片对应一个maptask
		 */
	}

	@Override
	public RecordReader<K, V> createRecordReader(InputSplit split,
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
		/**
		 * 实际读入方法，针对每个分片的每个maptask，会创建一个recordreader对象
		 */
	}
	
	public static class demorecordreader<K, V> extends RecordReader<K, V>{

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			/**
			 * 初始化方法
			 */
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return false;
			/**
			 * 判断当前分片有没有下一个keyvalue，如果有，一起读取
			 */
		}

		@Override
		public K getCurrentKey() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return null;
			/**
			 * 获取当前的key
			 */
		}

		@Override
		public V getCurrentValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return null;
			/**
			 * 获取当前的value
			 */
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return 0;
			//获取当前分片maptask处理的进度
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			//关闭资源
		}
		
	}
	
	public static class demoinputsplit extends InputSplit{

		@Override
		public long getLength() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return 0;
			//获取分片的长度
		}

		@Override
		public String[] getLocations() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return null;
			//用于数据本地化，获取当前数据所在的DN的地址（hostname）
			//如果不实现数据本地化，可以返回空数组
			//return new String[0];
		}
		
	}

}
