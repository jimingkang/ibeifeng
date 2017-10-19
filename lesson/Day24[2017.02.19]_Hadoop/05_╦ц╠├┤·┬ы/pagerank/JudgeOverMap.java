package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class JudgeOverMap extends Mapper<Object, Text, Text, Text> {

	@Override
	protected void map(Object key, Text value,Context context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub

		String[] splits = value.toString().split("\t")[0].split(":");
		context.write(new Text(splits[0]), new Text(splits[1]));
	}
}
