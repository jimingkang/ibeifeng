package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class PreProcessDataFormatReduce extends Reducer<Text, Text, Text, Text> {

	private int firstPR=1;
	
	@Override
	protected void setup(Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		this.firstPR =context.getConfiguration().getInt("first.PR.value", this.firstPR);
	}
	
	@Override
	protected void reduce(Text key, Iterable<Text> values,Context context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
		StringBuilder str = new StringBuilder();
		
		for(Text value : values){
			str.append(value.toString()).append(",");
		}
		context.write(new Text(key.toString()+":"+this.firstPR), 
				new Text(str.substring(0,str.length()-1)));
	}

}
