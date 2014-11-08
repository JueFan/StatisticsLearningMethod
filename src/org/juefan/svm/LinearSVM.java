package org.juefan.svm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.juefan.basic.FileIO;

public class LinearSVM {
	/**数据总量*/
	private int exampleNum;	
	/**维度*/
	private int exampleDim;	
	/**权向量*/
	private double[] w;	
	/**惩罚参数*/
	private double lambda;
	/**梯度下降率*/
	private double lr = 0.001;//0.00001
	/**停止条件*/
	private double threshold = 0.001;
	/**分类错误情况*/
	private double cost;	
	/**临时权向量*/
	private double[] grad;
	/**分类的值*/
	private double[] yp; 
	
	public LinearSVM(double paramLambda){
		lambda = paramLambda;		
	}
		
	/**参数更新*/
	private void update(){
		for(int d=0;d<exampleDim;d++){
			w[d] -= lr*grad[d];
		}
	}
	
	/**
	 * 模型训练
	 * @param datas 训练数据
	 * @param iterator 迭代次数
	 */
	public void Train(List<Data> datas, int iterator){
		exampleNum = datas.size();
		exampleDim = Data.dim.size();
		System.out.println("数据量：" + exampleNum + "\t数据维度：" + exampleDim);
		w = new double[exampleDim]; 
		grad = new double[exampleDim];
		yp = new double[exampleNum];
		
		for(int i = 0; i < iterator; i++){
			CostAndGrad(datas);
			if(i % 50 == 0)
			System.out.println("cost:"+cost);
			if(cost< threshold){
				break;
			}
		}
	}
	
	/**
	 * 损失函数和梯度下降求解
	 * @param datas	输入数据
	 */
	private void CostAndGrad(List<Data> datas){
		cost = 0;
		//计算分类错误情况
		for(int m=0;m<exampleNum;m++){
			yp[m]=0;
			for(int d=0;d<exampleDim;d++){
				//样本是否包含当前维度值
				if(datas.get(m).vector.containsKey(d))
				yp[m] += Double.parseDouble(datas.get(m).vector.get(d).toString())*w[d];
			}
			double ryp = Double.parseDouble(datas.get(m).label.toString());
			//合页损失函数
			if(ryp*yp[m]-1<0){
				cost += (1-ryp*yp[m]);
			}
		}
		
		//结构风险
		for(int d=0;d<exampleDim;d++){
			cost += 0.5*lambda*w[d]*w[d];
		}
		
		//参数的梯度下降法
		for(int d=0;d<exampleDim;d++){
			grad[d] = Math.abs(lambda*w[d]);	
			for(int m=0;m<exampleNum;m++){
				double ryp = Double.parseDouble(datas.get(m).label.toString());
				if(ryp*yp[m]-1<0){
					if(datas.get(m).vector.containsKey(d))
					grad[d]-= ryp*Double.parseDouble(datas.get(m).vector.get(d).toString());
				}
			}
		}	
		update();
	}
	
	/*分类预测*/	
	private int predict(Data data){
		double pre = 0;
		for(int i: data.vector.keySet()){
			if(i < w.length)
			pre += Double.parseDouble(data.vector.get(i).toString()) * w[i];
		}
		if(pre >= 0)
			return 1;
		else return -1;
	}
	
	/*数据测试*/
	public void Test(List<Data> datas){
		int error = 0;
		for(Data data: datas){
			if(predict(data) != Integer.parseInt(data.label.toString()))
				error++;
		}
		System.out.println("测试用例数：" + datas.size());
		System.out.println("错误个数：" + error);
		System.out.println("正确率：" + (double)(datas.size() - error)/datas.size());
	}
		
	public static void main(String[] args) throws IOException{
		
		FileIO file = new FileIO();
		file.setFileName("./file/svm.train3.txt");
		List<String> fileList = file.FileRead("utf-8");
		List<Data> datas = new ArrayList<>();
		for(String line: fileList)
			datas.add(new Data(line));
		LinearSVM svm1 = new LinearSVM(0.01);
		svm1.Train(datas, 100);

		file.setFileName("./file/svm.test3.txt");
		fileList = file.FileRead("utf-8");
		List<Data> test = new ArrayList<>();
		for(String line: fileList)
			test.add(new Data(line));
		svm1.Test(test);	
	}
}