package com.ibeifeng.bigdata.offline.analystics;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.ibeifeng.bigdata.offline.analystics.common.EventLogConstants;
import com.ibeifeng.bigdata.offline.analystics.util.HbaseScanUtil;


/**
 * 用于测试scan过滤器
 * @author beifeng
 *
 */
public class TestScan {

	private HbaseScanUtil scanutil = new HbaseScanUtil();
	
	@Test
	public void testScanUtil() throws IOException{
		
		Scan scan = scanutil.scanFilterUtil("20151220");
		Configuration conf = HBaseConfiguration.create();
		HTable table = new HTable(conf, EventLogConstants.HBASE_NAME_EVENT_LOGS+"20151220");
		ResultScanner rsscan = table.getScanner(scan);
		for(Result rs : rsscan){
			System.out.println(Bytes.toString(rs.getRow()));
			for(Cell cell : rs.rawCells()){
				System.out.println(
						Bytes.toString(CellUtil.cloneFamily(cell))
						+"->"+
						Bytes.toString(CellUtil.cloneQualifier(cell))
						+"->"+
						Bytes.toString(CellUtil.cloneValue(cell))
						+"->"+
						cell.getTimestamp()
						);
			}
			System.out.println("-----------------------------------------------------------");
		}
	}
	
}
