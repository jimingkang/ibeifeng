package org.apache.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 输入格式：1:1 2,3,4
 * 第一部分1：表示当前页面
 * 第二部分1：表示当前页面的pr值
 * 第三部分2,3,4：表示当前页面的出链
 * @author ibf
 *
 */

public class CalDataMap extends Mapper<Object, Text, CalOutputkey, Text> {

	public final int curvalue = 0;
	public final int nextvalue = 1;
	private CalOutputkey outputkey = new CalOutputkey();
	
	@Override
	protected void map(Object key, Text value,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String[] splits = value.toString().split("\t");
		
		String curPage = splits[0].split(":")[0];
		
		double pr = Double.valueOf(splits[0].split(":")[1]);

		if (splits.length == 2){
			//存在出链
			String[] nextPg = splits[1].split(",");
			int nextPgnum = nextPg.length;  //出链的个数
			double nextPr = pr / nextPgnum;//每个出链的pr值
			outputkey.setPr(nextPr);
			outputkey.setFlag(nextvalue);
			for (String nextpage : nextPg){
				outputkey.setCurPage(nextpage);
				context.write(outputkey, new Text(""));
			}
			outputkey.setPr(0);//当前页面输出
			outputkey.setFlag(curvalue);
			outputkey.setCurPage(curPage);
			context.write(outputkey, new Text(splits[1]));			
			
		}else{
			//不存在出链
			outputkey.setPr(0);//当前页面输出
			outputkey.setFlag(curvalue);
			outputkey.setCurPage(curPage);
			context.write(outputkey, new Text(""));	
		}
	}
}
