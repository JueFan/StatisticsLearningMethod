package org.juefan.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.juefan.basic.FileIO;
import org.juefan.data.Data;

public class SimpleKnn {
	
	public static final int K = 3;		
	public static int P = 2;		//距离函数的选择，P=2即欧氏距离
	
	public class LabelDistance{
		public double distance = 0;
		public int label;		
		public LabelDistance(double d, int l){
			distance = d;
			label = l;
		}
	}
	
	public sort compare = new sort();
	public class sort implements Comparator<LabelDistance> {
		public int compare(LabelDistance arg0, LabelDistance arg1) {
			return arg0.distance < arg1.distance ? -1 : 1;		//JDK1.7的新特性，返回值必须是一对正负数
		}
	}
	
	/**
	 * 俩个实例间的距离函数
	 * @param a
	 * @param b
	 * @return 返回距离值，如果俩个实例的维度不一致则返回一个极大值
	 */
	public double getLdistance(Data a, Data b){
		if(a.x.size() != b.x.size())
			return Double.MAX_VALUE;
		double inner = 0D;
		for(int i = 0; i < P; i++){
			inner += Math.pow(Double.valueOf(a.x.get(i).toString()) - Double.valueOf(b.x.get(i).toString())  , P);
		}
		return Math.pow(inner, (double)1/P);	
	}
	
	/**
	 * 计算实例与训练集的距离并返回最终判断结果
	 * @param d 待判断实例
	 * @param tran 训练集
	 * @return 实例的判断结果
	 */
	public int getLabelValue(Data d, ArrayList<Data> tran){
		ArrayList<LabelDistance> labelDistances= new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<>();
		int label = 0;
		int count = 0;
		for(Data data: tran){
			labelDistances.add(new LabelDistance(getLdistance(d, data), data.y));
		}
		Collections.sort(labelDistances, compare);
		for(int i = 0; i < K & i < labelDistances.size(); i++){
			System.out.println(labelDistances.get(i).distance + "\t" + labelDistances.get(i).label);
			int tmplabel = labelDistances.get(i).label;
			if(map.containsKey(tmplabel)){
				map.put(tmplabel, map.get(tmplabel) + 1);
			}else {
				map.put(tmplabel, 1);
			}
		}
		for(int key: map.keySet()){
			if(map.get(key) > count){
				count = map.get(key);
				label = key;
			}
		}
		return label;	
	}
	
	public static void main(String[] args) {
		SimpleKnn knn = new SimpleKnn();
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		fileIO.setFileName(".//file//knn.txt");
		fileIO.FileRead();
		for(String data: fileIO.fileList){
			datas.add(new Data(data));
		}
		Data data = new Data();
		data.x.add(2); data.x.add(1);
		System.out.println(knn.getLabelValue(data, datas));
	}
}
