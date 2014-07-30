package org.juefan.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.juefan.basic.FileIO;
import org.juefan.bayes.Data;

public class InfoGain {
	
	//返回底数为2的对数值
	public static double log2(double d){
		return Math.log(d)/Math.log(2);
	}
	
	/**
	 * 计算经验熵
	 * @param datas 当前数据集，可以为训练数据集中的子集
	 * @return 返回当前数据集的经验熵
	 */
	public  double getEntropy(ArrayList<Data> datas){
		int counts = datas.size();
		double entropy = 0;
		Map<Object, Double> map = new HashMap<Object, Double>();
		for(Data data: datas){
			if(map.containsKey(data.y)){
				map.put(data.y, map.get(data.y) + 1);
			}else {
				map.put(data.y, 1D);
			}
		}
		
		for(double v: map.values())
			entropy -= (v/counts * log2(v/counts));
		return entropy;
	}

	/**
	 * 计算条件熵
	 * @param datas 当前数据集，可以为训练数据集中的子集
	 * @param feature 待计算的特征位置
	 * @return 第feature个特征的条件熵
	 */
	public double getCondiEntropy(ArrayList<Data> datas, int feature){
		int counts = datas.size();
		double condiEntropy = 0;
		Map<Object, ArrayList<Data>> tmMap = new HashMap<>();
		for(Data data: datas){
			if(tmMap.containsKey(data.x.get(feature))){
				tmMap.get(data.x.get(feature)).add(data);
			}else {
				ArrayList<Data> tmDatas = new ArrayList<>();
				tmDatas.add(data);
				tmMap.put(data.x.get(feature), tmDatas);
			}
		}		
		for(ArrayList<Data> datas2: tmMap.values()){
			condiEntropy += (double)datas2.size()/counts * getEntropy(datas2);
		}
		return condiEntropy;
	}
	
	/**
	 * 计算信息增益（ID3算法）
	 * @param datas 当前数据集，可以为训练数据集中的子集
	 * @param feature 待计算的特征位置
	 * @return 第feature个特征的信息增益
	 */
	public double getInfoGain(ArrayList<Data> datas, int feature){
		return getEntropy(datas) - getCondiEntropy(datas, feature);
	}
	
	/**
	 * 计算信息增益率（C4.5算法）
	 * @param datas 当前数据集，可以为训练数据集中的子集
	 * @param feature 待计算的特征位置
	 * @return 第feature个特征的信息增益率
	 */
	public double getInfoGainRatio(ArrayList<Data> datas, int feature){
		return getInfoGain(datas, feature)/getEntropy(datas);
	}
	
	public static void main(String[] args) {
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		InfoGain tree = new InfoGain();
		fileIO.setFileName(".//file//decision.tree.txt");
		fileIO.FileRead();
		for(String data: fileIO.fileList){
			datas.add(new Data(data));
		}
		for(int i = 0; i < 4; i++){
			System.out.println("第" + i + "个特征的信息增益为：" + tree.getInfoGain(datas, i));
		}
	}
	
	
	
}
