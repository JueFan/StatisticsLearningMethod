package org.juefan.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.juefan.basic.FileIO;
/**
 * 这是一个简易的贝叶斯分类器
 * 只适用于离散数据，连续型数据的暂时请先绕道了^.^
 * @author JueFan
 */
public class NaiveBayes{
	
	//平滑指数, 默认为拉普拉斯平滑，极大似然估计则为0
	private static double Lambda  = 1; 
	//存储先验概率数据
	private Map<Object, Double> PriorProbability = new HashMap<>();
	//存储条件概率数据
	private Map<Object, ArrayList<Map<Object, Double>>> ConditionProbability = new HashMap<>();
	
	/**
	 * 计算类别的先验概率
	 * @param datas 
	 */
	public void setPriorPro(ArrayList<Data> datas){
		int counts = datas.size();
		for(Data data: datas){
			if(PriorProbability.containsKey(data.y)){
				PriorProbability.put(data.y, PriorProbability.get(data.y) + 1);
			}else {
				PriorProbability.put(data.y, (double) 1);
			}
		}
		for(Object o: PriorProbability.keySet())
			PriorProbability.put(o, (PriorProbability.get(o) + Lambda)/(counts + Lambda * PriorProbability.size()));
	}

	
	/**
	 * 计算条件概率
	 * @param datas
	 */
	public void setCondiPro(ArrayList<Data> datas){
		Map<Object, ArrayList<Data>> tmMap = new HashMap<>();
		//按类别先将数据分类存放
		for(Data data: datas){
			if(tmMap.containsKey(data.y)){
				tmMap.get(data.y).add(data);
			}else {
				ArrayList<Data> tmDatas = new ArrayList<>();
				tmDatas.add(data);
				tmMap.put(data.y, tmDatas);
			}
		}
		//条件概率主体
		for(Object o: tmMap.keySet()){
			ArrayList<Map<Object, Double>> tmCon = new ArrayList<>();
			int LabelCount = tmMap.get(o).size();
			//计算每个特征的相对频数
			for(Data data: tmMap.get(o)){
				for(int i = 0; i < data.x.size(); i++){
					if(tmCon.size() < i + 1){
						Map<Object, Double> tmMap2 = new HashMap<>();
						tmMap2.put(data.x.get(i), (double) 1);
						tmCon.add(tmMap2);
					}else {
						if(tmCon.get(i).containsKey(data.x.get(i))){
							tmCon.get(i).put(data.x.get(i), tmCon.get(i).get(data.x.get(i)) + 1);
						}else {
							tmCon.get(i).put(data.x.get(i),  (double) 1);
						}
					}
				}
			}
			//计算条件概率
			for(int i = 0; i < tmCon.size(); i++){
				for(Object o1: tmCon.get(i).keySet()){
					tmCon.get(i).put(o1, (tmCon.get(i).get(o1) + Lambda)/(LabelCount + Lambda * tmCon.get(i).size()));
				}
			}
			ConditionProbability.put(o, tmCon);
		}
	}
	
	/**
	 * 判断实例的类别
	 * @param data
	 * @return 判断结果
	 */
	public Object getLabel(Data data){
		Object label = new Object();
		double pro = 0D;
		for(Object o: PriorProbability.keySet()){
			double tmPro = 1;
			tmPro *= PriorProbability.get(o);
			for(int i = 0; i < data.x.size(); i++){
				tmPro *= ConditionProbability.get(o).get(i).get(data.x.get(i));
			}
			if(tmPro > pro){
				pro = tmPro;
				label = o;
			}
			System.out.println(o.toString() + " :的后验概率为: " + tmPro);
		}
		return label;
	}
	
	
	public static void main(String[] args) {
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		fileIO.setFileName(".//file//bayes.txt");
		fileIO.FileRead();
		for(String data: fileIO.fileList){
			datas.add(new Data(data));
		}
		
		NaiveBayes bayes = new NaiveBayes();
		bayes.setPriorPro(datas);
		bayes.setCondiPro(datas);
		
		Data data = new Data("1\t2\tS");
		System.out.println(data.toString() + "\t的判断类别为: " + bayes.getLabel(data));
	}

}
