package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PreProcessDataFormatMap extends Mapper<Object, Text, Text, Text> {

	@Override
	protected void map(Object key, Text value,
			Mapper<Object, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String line = value.toString();
		String[] splits = line.split("\t");
		
		context.write(new Text(splits[0]), new Text(splits[1]));		
	}
}
