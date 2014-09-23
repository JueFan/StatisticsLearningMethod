package org.juefan.svm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.juefan.basic.FileIO;

public class LinearSVM {
	private int exampleNum;	//数据总量
	private int exampleDim;	//维度
	private double[] w;	//权向量
	private double lambda;
	private double lr = 0.001;//0.00001
	private double threshold = 0.001;
	private double cost;	//分类错误情况
	private double[] grad;
	private double[] yp; //分类的值
	
	public LinearSVM(double paramLambda){
		lambda = paramLambda;		
	}
		
	/*参数更新*/
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
			if(i % 10 == 0)
			System.out.println("cost:"+cost);
			if(cost< threshold){
				break;
			}
			update();
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
				if(datas.get(m).vector.containsKey(d))
				yp[m] += Double.parseDouble(datas.get(m).vector.get(d).toString())*w[d];
			}
			
			if(Double.parseDouble(datas.get(m).label.toString())*yp[m]-1<0){
				cost += (1-Double.parseDouble(datas.get(m).label.toString())*yp[m]);
			}
		}
		
		//结构风险
		for(int d=0;d<exampleDim;d++){
			cost += 0.5*lambda*w[d]*w[d];
		}
		
		//不知道是什么东西
		for(int d=0;d<exampleDim;d++){
			grad[d] = Math.abs(lambda*w[d]);	
			for(int m=0;m<exampleNum;m++){
				if(Double.parseDouble(datas.get(m).label.toString())*yp[m]-1<0){
					if(datas.get(m).vector.containsKey(d))
					grad[d]-= Double.parseDouble(datas.get(m).label.toString())*Double.parseDouble(datas.get(m).vector.get(d).toString());
				}
			}
		}	
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
		LinearSVM svm1 = new LinearSVM(0.0001);
		svm1.Train(datas, 500);

		file.setFileName("./file/svm.test3.txt");
		fileList = file.FileRead("utf-8");
		List<Data> test = new ArrayList<>();
		for(String line: fileList)
			test.add(new Data(line));
		svm1.Test(test);	
	}
}