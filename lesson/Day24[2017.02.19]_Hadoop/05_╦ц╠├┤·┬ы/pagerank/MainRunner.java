package org.apache.hadoop.pagerank;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class MainRunner {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String basepath="/pagerank/in/data1";
		String[] preArgs = {"/pagerank/in/data1.txt",basepath+"/pre"};
		PreProcessDataRunner.main(preArgs);
		String[] calArgs = {basepath+"/pre",basepath+"/cal1"};
		CalRunner.main(calArgs);
		
		int index =1;
		while (!isover(basepath,index-1,index)){
			calArgs= new String[]{
				basepath + "/cal" + index,
				basepath + "/cal" + (index+1)
			};
			CalRunner.main(calArgs);
			index++;
		}
	}
	
	
	static boolean isover(String basepath , int firstindex,int secondindex) throws Exception{
		if(firstindex == 0){
			return false;
		}
		String[] judgeArgs = {
				basepath+"/cal"+firstindex,
				basepath+"/cal"+secondindex,
				basepath+"/flag"};
		JudgeOverRunner.main(judgeArgs);
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop.ibeifeng.com:8020");
		Path path = new Path(basepath+"/flag/part-r-00000");
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(FileSystem.get(conf).open(path)));
		try{
		if("true".equals(br.readLine().trim())){
			return true;
		}
		}finally{
			if (br!=null){
				try {
					br.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
		return secondindex>100;
		
	}
}
