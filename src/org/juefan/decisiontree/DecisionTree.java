package org.juefan.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.juefan.basic.FileIO;
import org.juefan.bayes.Data;

public class DecisionTree {
	public static final double e = 0.1;
	public InfoGain infoGain = new InfoGain();
	
	public TreeNode buildTree(ArrayList<Data> datas, ArrayList<String> featureName){
		TreeNode treeNode = new TreeNode();
		ArrayList<String> feaName = new ArrayList<>();
		feaName = featureName;
		if(isSingle(datas) || getMaxInfoGain(datas) < e){
			treeNode.setNodeName(getLabel(datas).toString());
			return treeNode;
		}else  {
			int feature = getMaxInfoGainFeature(datas);
			treeNode.setAttributeValue(feaName.get(feature + 1));
			ArrayList<String> tList = new ArrayList<>();
			tList = feaName;
			Map<Object, ArrayList<Data>> tMap = new HashMap<>();
			for(Data data: datas){
				if(tMap.containsKey(data.x.get(feature))){
					Data tData = new Data();
					for(int i = 0; i < data.x.size(); i++)
						if(i != feature)
							tData.x.add(data.x.get(i));
					tData.y = data.y;
					tMap.get(data.x.get(feature)).add(tData);
				}else {
					Data tData = new Data();
					for(int i = 0; i < data.x.size(); i++)
						if(i != feature)
							tData.x.add(data.x.get(i));
					tData.y = data.y;
					ArrayList<Data> tDatas = new ArrayList<>();
					tDatas.add(tData);
					tMap.put(data.x.get(feature),tDatas);
				}
			}
			List<TreeNode> treeNodes = new ArrayList<>();
			int child = 0;
			for(Object key: tMap.keySet()){
				//这一步太坑爹了，java的拷背坑真多啊，害我浪费了半天的时间
				ArrayList<String> tList2 = new ArrayList<>(tList);
				tList2.remove(feature + 1);
				treeNodes.add(buildTree(tMap.get(key), tList2));
				treeNodes.get(child ++).setTargetFunValue(key.toString());
			}
			treeNode.setChildTreeNode(treeNodes);
			feaName.remove(feature + 1);
		}	
		return treeNode;
	}
	
	/**
	 * 获取实例中的最大类
	 * @param datas 实例集
	 * @return 出现次数最多的类
	 */
	public Object getLabel(ArrayList<Data> datas){
		Map<Object, Integer> map = new HashMap<Object, Integer>();
		Object label = null;
		int max = 0;
		for(Data data: datas){
			if(map.containsKey(data.y)){
				map.put(data.y, map.get(data.y) + 1);
				if(map.get(data.y) > max){
					max = map.get(data.y);
					label = data.y;
				}
			}else {
				map.put(data.y, 1);
			}
		}
		return label;
	}
	
	/**
	 * 计算信息增益（率）的最大值
	 * @param datas
	 * @return 最大的信息增益值
	 */
	public double getMaxInfoGain(ArrayList<Data> datas){
		double max = 0;
		for(int i = 0; i < datas.get(0).x.size(); i++){
			double temp = infoGain.getInfoGain(datas, i);
			if(temp > max)
				max = temp;
		}
		return max;
	}
	
	/**信息增益最大的特征*/
	public int getMaxInfoGainFeature(ArrayList<Data> datas){
		double max = 0;
		int feature = 0;
		for(int i = 0; i < datas.get(0).x.size(); i++){
			double temp = infoGain.getInfoGain(datas, i);
			if(temp > max){
				max = temp;
				feature = i;
			}
		}
		return feature;
	}
	
	/**判断是否只有一类*/
	public boolean isSingle(ArrayList<Data> datas){
		Set<Object> set = new HashSet<>();
		for(Data data: datas)
			set.add(data.y);
		return set.size() == 1? true:false;
	}
	
	
	public static void main(String[] args) {
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		DecisionTree decisionTree = new DecisionTree();
		fileIO.setFileName(".//file//decision.tree.txt");
		fileIO.FileRead("utf-8");
		ArrayList<String> featureName = new ArrayList<>();
		//获取文件的标头
		for(String string: fileIO.fileList.get(0).split("\t"))
			featureName.add(string);
		for(int i = 1; i < fileIO.fileList.size(); i++){
			datas.add(new Data(fileIO.fileList.get(i)));
		}
		TreeNode treeNode = new TreeNode();
		treeNode = decisionTree.buildTree(datas, featureName);
		treeNode.printTree();
	}
	

}
