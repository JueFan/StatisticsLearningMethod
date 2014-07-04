package org.juefan.perceptron;


import java.util.ArrayList;

import org.juefan.basic.FileIO;
import org.juefan.data.Data;

public class PrimevalPerceptron {
	
	public static ArrayList<Double> w  = new ArrayList<>();
	public static int b ;
	
	/*初始化参数*/
	public PrimevalPerceptron(){
		w.add(5D);
		w.add(-2D);
		b = 3;
	}
	
	/**
	 * 判断是否分类正确
	 * @param data 待判断数据
	 * @return 返回判断正确与否
	 */
	public static boolean getValue(Data data){
		double state = 0;
		for(int i = 0; i < data.x.size(); i++){
			state += w.get(i) * (double)data.x.get(i);
		}
		state += b;
		return state * data.y > 0? true: false;	
	}
	
	//此算法基于数据是线性可分的，如果线性不可分，则会进入死循环
	public static boolean isStop(ArrayList<Data> datas){
		boolean isStop = true;
		for(Data data: datas){
			isStop = isStop && getValue(data);
		}
		return isStop;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		PrimevalPerceptron model = new PrimevalPerceptron();
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		fileIO.setFileName(".//file//perceptron.txt");
		fileIO.FileRead();
		for(String data: fileIO.fileList){
			datas.add(new Data(data));
		}
	
		/**
		 * 如果全部数据都分类正确则结束迭代
		 */
		while(!isStop(datas)){
			for(int i = 0; i < datas.size(); i++){
				if(!getValue(datas.get(i))){  //这里面可以理解为是一个简单的梯度下降法
					for(int j = 0; j < datas.get(i).x.size(); j++)
					w.set(j, w.get(j) + datas.get(i).y * (double)datas.get(i).x.get(j));
					b += datas.get(i).y;
					System.out.println(w + "\t" + b);
				}
			}
		}	
		System.out.println(w + "\t" + b);		//输出最终的结果
	}
}
