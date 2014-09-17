package org.juefan.svm;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
	
	private void CostAndGrad(double[][] X,double[] y){
		cost =0;
		for(int m=0;m<exampleNum;m++)
		{
			yp[m]=0;
			for(int d=0;d<exampleDim;d++)
			{
				yp[m]+=X[m][d]*w[d];
			}
			
			if(y[m]*yp[m]-1<0)
			{
				cost += (1-y[m]*yp[m]);
			}
			
		}
		
		for(int d=0;d<exampleDim;d++)
		{
			cost += 0.5*lambda*w[d]*w[d];
		}
		

		for(int d=0;d<exampleDim;d++)
		{
			grad[d] = Math.abs(lambda*w[d]);	
			for(int m=0;m<exampleNum;m++)
			{
				if(y[m]*yp[m]-1<0)
				{
					grad[d]-= y[m]*X[m][d];
				}
			}
		}				
	}
	
	private void update()
	{
		for(int d=0;d<exampleDim;d++)
		{
			w[d] -= lr*grad[d];
		}
	}
	
	private void updates(){
		for(int d = 0; d < exampleDim; d++)
			w[d] -= lr * grad[d];
	}
	
	public void Trains(List<Data> datas, int iterator){
		exampleNum = datas.size();
		exampleDim = Data.dim.size();
		System.out.println("数据量：" + exampleNum + "\t数据维度：" + exampleDim);
		w = new double[exampleDim];
		grad = new double[exampleDim];
		yp = new double[exampleNum];
		
		for(int i = 0; i < iterator; i++){
			CostAndGrads(datas);
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
	private void CostAndGrads(List<Data> datas){
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
	
	
	public void Train(double[][] X,double[] y,int maxIters)
	{
		exampleNum = X.length;
		if(exampleNum <=0) 
		{
			System.out.println("num of example <=0!");
			return;
		}
		exampleDim = X[0].length;
		w = new double[exampleDim];
		grad = new double[exampleDim];
		yp = new double[exampleNum];
		System.out.println("数据量：" + exampleNum + "\t数据维度：" + exampleDim);
		for(int iter=0;iter<maxIters;iter++)
		{
			
			CostAndGrad(X,y);
			//System.out.println("cost:"+cost);
			if(cost< threshold)
			{
				break;
			}
			update();
			
		}
	}
	private int predict(double[] x)
	{
		double pre=0;
		for(int j=0;j<x.length;j++)
		{
			pre+=x[j]*w[j];
		}
		if(pre >=0)//这个阈值一般位于-1到1
			return 1;
		else return -1;
	}
	
	public void Test(double[][] testX,double[] testY)
	{
		int error=0;
		for(int i=0;i<testX.length;i++)
		{
			if(predict(testX[i]) != testY[i])
			{
				error++;
			}
		}
		System.out.println("total:"+testX.length);
		System.out.println("error:"+error);
		System.out.println("error rate:"+((double)error/testX.length));
		System.out.println("acc rate:"+((double)(testX.length-error)/testX.length));
	}
	
	
	
	public static void loadData(double[][]X,double[] y,String trainFile) throws IOException
	{
		
		File file = new File(trainFile);
		RandomAccessFile raf = new RandomAccessFile(file,"r");
		StringTokenizer tokenizer,tokenizer2; 

		int index=0;
		while(true)
		{
			String line = raf.readLine();
			
			if(line == null) break;
			tokenizer = new StringTokenizer(line," ");
			y[index] = Double.parseDouble(tokenizer.nextToken());
			//System.out.println(y[index]);
			while(tokenizer.hasMoreTokens())
			{
				tokenizer2 = new StringTokenizer(tokenizer.nextToken(),":");
				int k = Integer.parseInt(tokenizer2.nextToken());
				double v = Double.parseDouble(tokenizer2.nextToken());
				X[index][k] = v;
				//System.out.println(k);
				//System.out.println(v);				
			}	
			X[index][0] =1;
			index++;		
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		/*double[] y = new double[400];
		double[][] X = new double[400][11];
		String trainFile = ".\\file\\svm.tran1.txt";
		loadData(X,y,trainFile);
		
		
		LinearSVM svm = new LinearSVM(0.0001);
		svm.Train(X,y,7000);*/
		

		
		FileIO file = new FileIO();
		file.setFileName("./file/svm.tran1.txt");
		List<String> fileList = file.FileRead("utf-8");
		List<Data> datas = new ArrayList<>();
		for(String line: fileList)
			datas.add(new Data(line));
		LinearSVM svm1 = new LinearSVM(0.001);
		svm1.Trains(datas, 1000);

		double[] test_y = new double[283];
		double[][] test_X = new double[283][11];
		String testFile = ".\\file\\svm.test2.txt";
		loadData(test_X,test_y,testFile);
		svm1.Test(test_X, test_y);
		
	}

}