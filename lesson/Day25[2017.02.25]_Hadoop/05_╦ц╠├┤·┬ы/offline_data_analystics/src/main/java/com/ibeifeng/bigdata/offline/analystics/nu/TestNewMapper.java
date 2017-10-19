package com.ibeifeng.bigdata.offline.analystics.nu;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.ibeifeng.bigdata.offline.analystics.common.DateEnum;
import com.ibeifeng.bigdata.offline.analystics.common.EventLogConstants;
import com.ibeifeng.bigdata.offline.analystics.common.GlobalConstants;
import com.ibeifeng.bigdata.offline.analystics.common.KpiType;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.base.BrowserDimension;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.base.DateDimension;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.base.KpiDimension;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.base.PlatformDimension;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.stats.StatsCommonDimension;
import com.ibeifeng.bigdata.offline.analystics.dimension.key.stats.StatsUserDimension;
import com.ibeifeng.bigdata.offline.analystics.util.TimeUtil;

/**
 * hbase与MapReduce集成
 * 		-》map端继承tablemapper
 * 			-》默认的输入
 * 				key:rowkey
 * 				value：result（对应rowkey的所有数据）
 * 			—》数据类型
 * 				ImmutableBytesWritable, Result
 * @author beifeng
 *
 */
public class TestNewMapper extends TableMapper<StatsUserDimension, Text> {
	
	private StatsUserDimension outputKey  = new StatsUserDimension();
	private Text outputValue = new Text();
	private BrowserDimension defaultBrowser = new BrowserDimension("", "");
	private KpiDimension userKpi = new KpiDimension(KpiType.NEW_INSTALL_USER.name);
	private KpiDimension browserKpi = new KpiDimension(KpiType.BROWSER_NEW_INSTALL_USER.name);
	private long startDay,stopDay;
	private long startWeek,stopWeek;
	private long startMonth,stopMonth;
	
	
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String dateTime = context.getConfiguration().get(GlobalConstants.RUNNING_DATE_PARAMES);
		long date = TimeUtil.parseString2Long(dateTime, TimeUtil.DATE_FORMAT);
		this.startDay = date;
		this.stopDay = date + GlobalConstants.DAY_OF_MILLISECONDS;
		this.startWeek = TimeUtil.getFirstDayOfThisWeek(date);
		this.stopWeek = TimeUtil.getFirstDayOfNextWeek(date);
		this.startMonth = TimeUtil.getFirstDayOfThisMonth(date);
		this.stopMonth = TimeUtil.getFirstDayOfNextMonth(date);
		
	}

	@Override
	protected void map(
			ImmutableBytesWritable key,
			Result value,Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		//获取value中需要的所有字段
		String uuid = this.getHbaseValue(value,EventLogConstants.LOG_COLUMN_NAME_UUID);
		String s_time = this.getHbaseValue(value, EventLogConstants.LOG_COLUMN_NAME_SERVER_TIME);
		long time = TimeUtil.parseNginxServerTime2Long(s_time);
		String plname = this.getHbaseValue(value, EventLogConstants.LOG_COLUMN_NAME_PLATFORM);
		String plversion = this.getHbaseValue(value, EventLogConstants.LOG_COLUMN_NAME_VERSION);
		String browsername = this.getHbaseValue(value, EventLogConstants.LOG_COLUMN_NAME_BROWSER_NAME);
		String browserversion = this.getHbaseValue(value, EventLogConstants.LOG_COLUMN_NAME_BROWSER_VERSION);
		
		if(uuid == null || s_time == null){
			return;
		}
		
		/**
		 * 维度的封装
		 */
		//时间维度的封装（天，周，月，季度）
		DateDimension dayDimension = DateDimension.buildDate(time, DateEnum.DAY);
		DateDimension weekDimension = DateDimension.buildDate(time, DateEnum.WEEK);
		DateDimension monthDimension = DateDimension.buildDate(time,DateEnum.MONTH);
		
		//平台维度
		//（name,all）(name,version)
		List<PlatformDimension> platforms = PlatformDimension.buildList(plname, plversion);
		
		//浏览器维度
		List<BrowserDimension> browsers = BrowserDimension.buildList(browsername, browserversion);
		
		/**
		 * 输出
		 */
		//输出value
		this.outputValue.set(uuid);
		//输出key
		/**
		 *   key:statscommondimension(datedimension,platformdimension,kpidimension)
		 *   	 browserdimension
		 */
		StatsCommonDimension statsCommon = this.outputKey.getStatsCommon();
		//设置stats_user浏览器维度,占位
		this.outputKey.setBrowser(this.defaultBrowser);
		for(PlatformDimension platform : platforms){
			//设置平台维度
			statsCommon.setPlatform(platform);
			statsCommon.setKpi(userKpi);
			//时间维度
			if(time >= this.startDay && time < this.stopDay){
				statsCommon.setDate(dayDimension);
				context.write(this.outputKey, this.outputValue);
			}
			if(time >= this.startWeek && time < this.stopWeek){
				statsCommon.setDate(weekDimension);
				context.write(this.outputKey, this.outputValue);
			}
			if(time >= this.startMonth && time < this.stopMonth){
				statsCommon.setDate(monthDimension);
				context.write(this.outputKey, this.outputValue);
			}
			
			
			/**
			 * 设置stats_browser的浏览器维度
			 */
			statsCommon.setKpi(browserKpi);
			for(BrowserDimension browser : browsers){
				this.outputKey.setBrowser(browser);
				//时间维度
				if(time >= this.startDay && time < this.stopDay){
					statsCommon.setDate(dayDimension);
					context.write(this.outputKey, this.outputValue);
				}
				if(time >= this.startWeek && time < this.stopWeek){
					statsCommon.setDate(weekDimension);
					context.write(this.outputKey, this.outputValue);
				}
				if(time >= this.startMonth && time < this.stopMonth){
					statsCommon.setDate(monthDimension);
					context.write(this.outputKey, this.outputValue);
				}
			}
		}
		
		
	}

	public String getHbaseValue(Result value, String logColumn) {
		// TODO Auto-generated method stub
		return Bytes.toString(value.getValue(
				EventLogConstants.BYTES_EVENT_LOGS_FAMILY_NAME, 
				Bytes.toBytes(logColumn))
				);
	}
	
}
