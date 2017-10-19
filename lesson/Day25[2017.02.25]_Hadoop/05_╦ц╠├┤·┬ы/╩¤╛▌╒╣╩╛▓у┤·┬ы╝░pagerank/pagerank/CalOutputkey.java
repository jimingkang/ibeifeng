package org.apache.hadoop.pagerank;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class CalOutputkey implements WritableComparable<CalOutputkey> {

	private String curPage;
	private int flag;
	private double pr;
	
	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public double getPr() {
		return pr;
	}

	public void setPr(double pr) {
		this.pr = pr;
	}


	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(this.curPage);
		out.writeInt(this.flag);
		out.writeDouble(this.pr);
	}


	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.curPage = in.readUTF();
		this.flag = in.readInt();
		this.pr = in.readDouble();
	}


	public int compareTo(CalOutputkey o) {
		// TODO Auto-generated method stub
		return this.curPage.compareTo(o.curPage);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.curPage+":"+this.pr;
	}

}
