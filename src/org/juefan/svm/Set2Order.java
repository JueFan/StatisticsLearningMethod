package org.juefan.svm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.juefan.basic.FileIO;

public class Set2Order {
	
	public static int num = 0;	//自增序号
	
	public Set<Integer> fieldDrop = new HashSet<Integer>();	//不参与计算的列序列
	
	public static Map<Integer, String> fieldMap = new HashMap<Integer, String>();	//存储每个列的列名
	
	public static List<Map<String,Integer>> valueMap = new ArrayList<Map<String,Integer>>();//存储每个列离散值的集合及其对应的序号
	
	public static Map<Integer, String> numMap = new HashMap<Integer, String>();	//序列与值的对应关系

	
	/**
	 * 读取表字段名称
	 * @param line
	 */
	public Set2Order(String line){
		String[] strings = line.split("\t");
		for(int i = 0; i < strings.length; i++){
			fieldMap.put(i, strings[i]);
			Map<String, Integer> tMap = new HashMap<String, Integer>();
			valueMap.add(tMap);
		}		
	}
	
	/**
	 * 生成数据映射关系
	 * @param line	用户的特征数据
	 */
	public void addDataStruct(String line){
		String[] strings = line.split("\001");
		for(int i = 1; i < strings.length; i++){
			if(!fieldDrop.contains(i) && !strings[i].equals("\\N") && !strings[i].equals("null")){
				String[] strings2 = strings[i].split(",");
				for(String string: strings2){
					setValueMap(i, string);
				}
			}
		}
	}
	
	/**
	 * 生成数据映射关系
	 * @param n	第n列
	 * @param v	列的值
	 */
	public void setValueMap(int n, String v){
		if(n > valueMap.size())
			System.err.println("长度超出");
		if(!valueMap.get(n).containsKey(v)){
			valueMap.get(n).put(v, num);
			numMap.put(num, v);
			num++;		
		}
	}
	
	public static void main(String[] args) {
		FileIO fileIO = new FileIO();
		fileIO.setFileName(".//file//widetable//table_struct.txt");
		fileIO.FileRead("utf-8");
		List<String> featureName = new ArrayList<>();
		featureName = fileIO.fileList;
		Set2Order set2Order = new Set2Order(featureName.get(0));
		set2Order.fieldDrop.add(0);
		set2Order.fieldDrop.add(1);
		set2Order.fieldDrop.add(2);
		set2Order.fieldDrop.add(3);
		set2Order.fieldDrop.add(4);
		set2Order.fieldDrop.add(141);
		fileIO.setFileName(".//file//widetable//test1.txt");
		fileIO.FileRead("utf-8");
		System.out.println(fieldMap);
		for(String string: featureName)
			set2Order.addDataStruct(string);
		System.out.println(numMap);
	}
	
	
	
	

}
