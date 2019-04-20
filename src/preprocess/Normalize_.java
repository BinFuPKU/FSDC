package preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Standardize;

public class Normalize_ {

	//weka.filters.unsupervised.attribute.Normalize
	public static Instances [] normalize_(Instances train, Instances test){
		Normalize n=new Normalize();
		try {
			n.setInputFormat(train);
			train=n.useFilter(train, n);
			test=n.useFilter(test, n);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		double [] maxarray=n.getMaxArray();
//		double [] minarray=n.getMinArray();
//		System.out.println(maxarray.length+"\t"+minarray.length);
//		for (double x : maxarray)
//			System.out.println(x);
		
//		for (int i=0;i<maxarray.length-1;i++){//attributes
//			double max=maxarray[i];
//			double min=minarray[i];
//			if (max==min)
//				continue;
//			//train
//			for (int j=0;j<train.numInstances();j++){//instance
//				Instance ins=train.get(j);
//				ins.setValue(i, (ins.value(i)-min)/(max-min));
//				train.set(j, ins);
//			}
//			//test
//			for (int j=0;j<test.numInstances();j++){//instance
//				Instance ins=test.get(j);
//				if (ins.value(i)>max)
//					ins.setValue(i,1.0);
//				else if (ins.value(i)<min)
//					ins.setValue(i,0.0);
//				else
//					ins.setValue(i, (ins.value(i)-min)/(max-min));
//				test.set(j, ins);
//			}
//		}
		Instances [] rs=new Instances[2];
		rs[0]=train;
		rs[1]=test;
		return rs;
	}
	
	//weka.filters.unsupervised.attribute.Normalize
	public static Instances [] standardize_(Instances train, Instances test){
		Standardize s=new Standardize();
		try {
			s.setInputFormat(train);
			train=s.useFilter(train,s);
			test=s.useFilter(test,s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instances [] rs=new Instances[2];
		rs[0]=train;
		rs[1]=test;
		return rs;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File trainfile= new File("D:\\聚类论文\\源代码\\10_\\CTG-10-fold\\1_train.arff");
		File testfile= new File("D:\\聚类论文\\源代码\\10_\\CTG-10-fold\\1_test.arff");

		ArffLoader loader = new ArffLoader();
		ArffSaver saver = new ArffSaver();
        try {
			loader.setFile(trainfile);
	        Instances train = loader.getDataSet();
	        train.setClassIndex(train.numAttributes()-1);
	        
	        loader.setFile(testfile);
	        Instances test = loader.getDataSet();
	        test.setClassIndex(test.numAttributes()-1);
	        
	        System.out.println(train.numAttributes()+"\t"+test.numAttributes());
	        Instances [] rs=standardize_(train,test);
	        
	        saver.setInstances(rs[0]);  
	        saver.setFile(new File("E:\\聚类论文\\源代码\\10_\\CTG-10-fold\\1_train_.arff"));  
	        saver.writeBatch();
	        
	        saver.setInstances(rs[1]);  
	        saver.setFile(new File("E:\\聚类论文\\源代码\\10_\\CTG-10-fold\\1_test_.arff"));  
	        saver.writeBatch(); 
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
