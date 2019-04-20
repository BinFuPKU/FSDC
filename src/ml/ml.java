package ml;

import java.io.File;
import java.io.IOException;

import tools.IO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ml {

//	public static double [] Logistic_(Instances train, Instances test) {
//		Logistic lg = new Logistic();
//		Evaluation eval = null;
//		try {
//			lg.buildClassifier(train);
//			eval = new Evaluation(train);
//			eval.evaluateModel(lg, test);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		double [] rs=new double[3];
//		rs[0]=1-eval.errorRate();
//		rs[1]=eval.weightedAreaUnderROC();
//		rs[2]=eval.weightedFMeasure();
//		return rs;
//	}
//	
//// RandomForest_
//	public static double [] RandomForest_(Instances train, Instances test,int n) {
//		RandomForest rf = new RandomForest();
//		rf.setNumTrees(n);
//		Evaluation eval = null;
//		try {
//			rf.buildClassifier(train);
//			eval = new Evaluation(train);
//			eval.evaluateModel(rf, test);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		double [] rs=new double[3];
//		rs[0]=1-eval.errorRate();
//		rs[1]=eval.weightedAreaUnderROC();
//		rs[2]=eval.weightedFMeasure();
//		return rs;
//	}
//	// SMO_
//	public static double [] SMO_(Instances train, Instances test) {
//		SMO smo = new SMO();
//		Evaluation eval = null;
//		try {
//			smo.buildClassifier(train);
//			eval = new Evaluation(train);
//			eval.evaluateModel(smo, test);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		double [] rs=new double[3];
//		rs[0]=1-eval.errorRate();
//		rs[1]=eval.weightedAreaUnderROC();
//		rs[2]=eval.weightedFMeasure();
//		return rs;
//	}
//	// MultilayerPerceptron_
//	public static double [] MultilayerPerceptron_ (Instances train, Instances test) {
//		MultilayerPerceptron mlp = new MultilayerPerceptron();
//		Evaluation eval = null;
//		try {
//			mlp.buildClassifier(train);
//			eval = new Evaluation(train);
//			eval.evaluateModel(mlp, test);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		double [] rs=new double[3];
//		rs[0]=1-eval.errorRate();
//		rs[1]=eval.weightedAreaUnderROC();
//		rs[2]=eval.weightedFMeasure();
//		return rs;
//	}
	
	//dis deal  [7][3]
	public static double [][] deal(Instances train,Instances test){
		// ibk  nb j48 logistic rf smo mlp
		double [][] rs = new double [3][3];
		rs[0]=IBK_.run(train,test);
		rs[1]=NaiveBayes_.run(train,test);
		rs[2]=J48_.run(train,test);
//		rs[3]=Logistic_(train,test);
//		rs[4]=RandomForest_(train,test,100);
//		rs[5]=SMO_(train,test);
//		rs[6]=MultilayerPerceptron_(train,test);
		
		return rs;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String indirpath="D:\\聚类论文\\源代码\\10_";
		String outdirpath="D:\\聚类论文\\源代码\\result\\kpp";
		ArffLoader loader = new ArffLoader();   
		
		for (File subdir:new File(indirpath).listFiles()){
			for (int i=1;i<11;i++){
				String trainpath=subdir.getAbsolutePath()+"\\"+i+"_train.arff";
				String testpath=subdir.getAbsolutePath()+"\\"+i+"_test.arff";
				try {
					loader.setFile(new File(trainpath));

			        Instances train = loader.getDataSet();
			        train.setClassIndex(train.numAttributes()-1);
			        
			        loader.setFile(new File(testpath));
			        Instances test = loader.getDataSet();
			        test.setClassIndex(test.numAttributes()-1);
			        
			        double [][] rs=deal(train,test);
			        
			        IO.append(outdirpath+"\\"+subdir.getName()+".all", rs[0]+"\t"+rs[1]+"\t"+rs[2]+"\n");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
