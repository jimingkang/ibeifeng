package com.ibeifeng.bigdata.offline.analystics.nu;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.ibeifeng.bigdata.offline.analystics.dimension.key.stats.StatsUserDimension;

public class TestNewReduce extends Reducer<StatsUserDimension, Text, StatsUserDimension, LongWritable> {
	
	private Set<String> uuidSet = new HashSet<String>();
	private LongWritable outputValue = new LongWritable(); 

	@Override
	protected void reduce(
			StatsUserDimension key,
			Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		this.uuidSet.clear();
		for(Text value : values){
			this.uuidSet.add(value.toString());
		}
		long counter = this.uuidSet.size();
		this.outputValue.set(counter);
		context.write(key, this.outputValue);
	}
}
