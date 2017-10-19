package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CalDataRudece extends Reducer< CalOutputkey, Text, CalOutputkey, Text> {
	
	private static final int currentvalue=0;
	private static final int nextvalue=1;
	private CalOutputkey outputkey = new CalOutputkey();
	private Text outputvalue = new Text();
	private double q=0.85;
	
	@Override
	protected void setup(Reducer<CalOutputkey, Text, CalOutputkey, Text>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.setup(context);
		this.q = context.getConfiguration().getDouble("pagerank.q.value", this.q);
	}
	
	@Override
	protected void reduce(CalOutputkey key, Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		int N=0;
		double sumPr=0;
		for (Text value:values){
			if(key.getFlag() == CalDataRudece.currentvalue){
				this.outputkey.setCurPage(key.getCurPage());
				this.outputvalue.set(value.toString());
			}else{
				//计算pr值
				N++;
				sumPr += key.getPr();
			}
		}
		double pr = 0;
		if(N>0){
			pr = 1.0 * (1-this.q) / N  + this.q * sumPr;
		}
		this.outputkey.setPr(pr);
		context.write(this.outputkey, this.outputvalue);
	}
}
