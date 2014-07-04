package org.juefan.data;

import java.util.ArrayList;

public class Data {
	public ArrayList<Object> x;
	public int y;
	
	/**读取一行数据转化为标准格式*/
	public Data(String content){
		String[] strings = content.split("\t| |:");
		ArrayList<Object> xList = new ArrayList<Object>();
		for(int i = 1; i < strings.length; i++){
			xList.add(Double.parseDouble(strings[i]));
		}
		this.x = new ArrayList<>();
		this.x = xList;
		this.y = Integer.parseInt(strings[0]);
	}
	
	public Data(){
		x  = new ArrayList<>();
		y = 0;
	}
	
	/**返回俩个点的内积*/
	public static double getInner(Data a, Data b){
		if(a.x.size() != b.x.size())
			return 0;
		double inner = 0;
		for(int i = 0; i < a.x.size(); i++){
			inner += (double)a.x.get(i) * (double)b.x.get(i);
		}
		return inner;
	}
}
