package org.juefan.perceptron;


import java.util.ArrayList;

public class PrimevalPerceptron {
	
	public static ArrayList<Integer> w  = new ArrayList<>();
	public static int b ;
	
	/*初始化参数*/
	public PrimevalPerceptron(){
		w.add(0);
		w.add(0);
		b = 0;
	}
	
	/**
	 * 判断是否分类正确
	 * @param data 待判断数据
	 * @return 返回判断正确与否
	 */
	public static boolean getValue(Data data){
		int state = 0;
		for(int i = 0; i < data.x.size(); i++){
			state += w.get(i) * data.x.get(i);
		}
		state += b;
		return state * data.y > 0? true: false;	
	}
	
	public static void main(String[] args) {
		PrimevalPerceptron model = new PrimevalPerceptron();
		ArrayList<Data> datas = new ArrayList<>();
		ArrayList<Integer> vList1 = new ArrayList<>();
		vList1.add(3);
		vList1.add(3);
		datas.add(new Data(vList1, 1));
		ArrayList<Integer> vList2 = new ArrayList<>();
		vList2.add(4);
		vList2.add(3);
		datas.add(new Data(vList2, 1));
		ArrayList<Integer> vList3 = new ArrayList<>();
		vList3.add(1);
		vList3.add(1);
		datas.add(new Data(vList3, -1));
		
		/**
		 * 如果全部数据都分类正确则结束迭代
		 */
		while(!(getValue(datas.get(0)) && getValue(datas.get(1)) && getValue(datas.get(2)))){
			for(int i = 0; i < 3; i++){
				if(!getValue(datas.get(i))){
					for(int j = 0; j < 2; j++)
					w.set(j, w.get(j) + datas.get(i).y * datas.get(i).x.get(j));
					b += datas.get(i).y;
				}
			}
		}
		
		System.out.println(w + "\t" + b);		//输出最终的结果
	}

}
