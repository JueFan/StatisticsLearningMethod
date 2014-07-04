package org.juefan.perceptron;
import java.util.ArrayList;
import org.juefan.basic.FileIO;
import org.juefan.data.Data;
public class GramPerceptrom {
	
	public static ArrayList<Integer> a  = new ArrayList<>();
	public static int b ;
	
	/*初始化参数*/
	public GramPerceptrom(int num){
		for(int i = 0; i < num; i++)
			a.add(0);
		b = 0;
	}
	/**Gram矩阵*/
	public static ArrayList<ArrayList<Object>> gram = new ArrayList<>();
	public void setGram(ArrayList<Data> datas){
		for(int i = 0; i < datas.size(); i++){
			ArrayList<Object> rowGram = new ArrayList<>();
			for(int j = 0; j < datas.size(); j++){
				rowGram.add(Data.getInner(datas.get(i), datas.get(j)));
			}
			gram.add(rowGram);
		}
	}
	
	/**是否正确分类*/
	public static boolean isCorrect(int i, ArrayList<Data> datas){
		double value = 0;
		for(int j = 0; j < datas.size(); j++)
			value += a.get(j)*datas.get(j).y * (double)gram.get(j).get(i);
		value = datas.get(i).y * (value + b);
		return value > 0 ? true: false;
	}
	
	//此算法基于数据是线性可分的，如果线性不可分，则会进入死循环
	public static boolean isStop(ArrayList<Data> datas){
		boolean isStop = true;
		for(int i = 0; i < datas.size(); i++){
			isStop = isStop && isCorrect(i, datas);
		}
		return isStop;
	}
	
	public static void main(String[] args) {
		ArrayList<Data> datas = new ArrayList<>();
		FileIO fileIO = new FileIO();
		fileIO.setFileName(".//file//perceptron.txt");
		fileIO.FileRead();
		for(String data: fileIO.fileList){
			datas.add(new Data(data));
		}
		GramPerceptrom gram  = new GramPerceptrom(datas.size());
		gram.setGram(datas);
		System.out.println(datas.size());
		while(!isStop(datas)){
			for(int i = 0; i < datas.size(); i++)
				if(!isCorrect(i, datas)){
					a.set(i, a.get(i) + 1);
					b += datas.get(i).y;
					System.out.println(a + "\t" + b);
				}
		}
	}
}
