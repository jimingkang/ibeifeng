package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * sqrt (1-1)^2 + (2-2)^2 + (3-3)^2+ (4-4)^2 < 给定的某个值
 * 我们就认为其收敛了
 */

public class JudgeOverReduce extends Reducer<Text, Text, Text, Text> {

	private double limit =0.001;
	
	@Override
	protected void setup(Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		this.limit=context.getConfiguration().getDouble("limit", this.limit);
		if (Double.compare(this.limit, 0)<0){
			this.limit=0.00001;
		}
	}
	
	@Override
	protected void reduce(Text key, Iterable<Text> values,Context context) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
		String lastkey =null;
		double lastvalue= -1;
		double sum=0;
		
		for (Text value : values){
			if(lastkey == null){
				lastkey=key.toString();
				lastvalue=Double.valueOf(value.toString());
			}else{
				sum += Math.pow(lastvalue - Double.valueOf(value.toString()),2);
				lastkey=null;
				lastvalue=0;
			}
		}
		sum = Math.sqrt(sum);
		if(Double.compare(sum, limit)>0){
			//sum大，没有收敛
			context.write(new Text("false"), new Text(""));
		}else{
			context.write(new Text("true"), new Text(""));
		}
		
	}
}
