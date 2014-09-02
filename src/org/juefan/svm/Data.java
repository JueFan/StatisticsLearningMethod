package org.juefan.svm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.juefan.basic.FileIO;

public class Data {
	public Object label;
	public Map<Integer, Object> vector = new LinkedHashMap<Integer, Object>();

	public Data(String line){
		String[] lines = line.split("\t");
		label = lines[0];
		for(int i = 1; i < lines.length; i++){
			String[] vec = lines[i].split(":");
			vector.put(Integer.parseInt(vec[0]), vec[1]);
		}
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(label.toString()).append("\t");
		for(int vec: vector.keySet()){
			buffer.append(vec).append(":").append(vector.get(vec)).append("\t");
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		FileIO file = new FileIO();
		file.setFileName("./file/svm.test1.txt");
		List<String> fileList = new ArrayList<>();
		fileList = file.FileRead("utf-8");
		List<Data> datas = new ArrayList<>();
		for(String line: fileList)
			datas.add(new Data(line));
		for(Data data: datas)
			System.out.println(data.toString());
	}
}
