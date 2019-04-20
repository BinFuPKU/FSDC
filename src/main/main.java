package main;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import cluster.MyClusterer;
import ml.ml;
import preprocess.Filter_;
import preprocess.Normalize_;
import excel.BiTuples;
import excel.ExcelGather;
import excel.ToExcel;
import select.SelectAttr;
import select.Select_;
import tools.IO;
import tools.InstancesFromFile;
import tools.LoadStart;
import weka.core.Instances;

public class main {
	private static SelectAttr sa;
	private static String window_len;

	public static ToExcel Deal(String trainPath,String testPath,int currentFold,ToExcel te,String path){
		Instances train = InstancesFromFile.instancesFromARFF(trainPath);
		Instances test = InstancesFromFile.instancesFromARFF(testPath);
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		te.setNumAttrsOrigin(train.numAttributes()-1);
		//preprocess
		//normalize
		Instances [] rs = Normalize_.normalize_(train, test);
		train = rs[0];test = rs[1];
		
		//deal:select
		if (window_len.equalsIgnoreCase("logN"))
			sa.setWindow_len((int)Math.log(train.numInstances()));
		else if (window_len.equalsIgnoreCase("sqrtN"))
			sa.setWindow_len((int)Math.sqrt(train.numInstances()));
			
		BiTuples sls=sa.deal(train,currentFold,path);
		Instances newtrain = Filter_.filterLeftClass(train, sls.getSelect());
		Instances newtest = Filter_.filterLeftClass(test, sls.getSelect());
		//7*3
		double [][] select_results=ml.deal(newtrain,newtest);
		
		te.add(sls.getSelect(), select_results);
		
		//write ml
		LoadStart.save_ml(path+".ml",currentFold, sls.getSelect(), select_results);
		
		System.gc();
		
		return te;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length==1 && args[0].equalsIgnoreCase("--help")){
			System.out.println("输入参数个数4:(startDataSet,delta,window_len,cluster)");
			System.out.println("\tstartDataSet:起始数据集名称（文件夹）");
			System.out.println("\tdelta:(0,1]");
			System.out.println("\twindow_len:(logN|sqrtN)");
			System.out.println("\tcluster:"+MyClusterer.clusterers);
			System.exit(0);
		}
		else if (args.length!=4 || !(args[2].equalsIgnoreCase("logN") || args[2].equalsIgnoreCase("sqrtN"))){
			System.out.println("输入参数个数不为4或不合法 (startDataSet、delta((0,1])、window_len(logN|sqrtN)、cluster)！");
			System.out.println("\t输入参数个数4:(startDataSet,delta,window_len,cluster)");
			System.out.println("\tstartDataSet:起始数据集名称（文件夹）");
			System.out.println("\tdelta:(0,1]");
			System.out.println("\twindow_len:(logN|sqrtN)");
			System.out.println("\tcluster:"+MyClusterer.clusterers);
			System.exit(1);
		}
		
		final String indirpath="../10_";
		final String startdir=args[0];
		sa = new SelectAttr(Double.parseDouble(args[1]),args[3].trim());
		window_len=args[2];
		
		final String logdirpath="log/"+args[3]+"_"+args[1]+"_"+args[2];
		final String exceldirpath="excel/"+args[3]+"_"+args[1]+"_"+args[2];
		
		//"I:/FS/mdbc/excel/mdbc_0.4_logN"；
		final String inExcelDirPath=exceldirpath;
		final String allExcelFilePath="../libs/result(all).txt";
		final String outExcelGatherFilePath="../libs/Combine_.xls";
		
		{
			File outdir=new File(logdirpath);
			if  (!outdir.exists())
				outdir.mkdirs();
			outdir=new File(exceldirpath);
			if  (!outdir.exists())
				outdir.mkdirs();
		}
		Date d0,d1,d11 =null;
		d0 = new Date();
		boolean flag=true;
		for (File subdir:new File(indirpath).listFiles()){
			d1 = new Date();
			System.out.println(subdir.getName());
			if (subdir.getName().equals(startdir))
				flag=false;
			if (flag)
				continue;
			ToExcel te= new ToExcel(exceldirpath+"\\"+subdir.getName()+".xls");
			//恢复(fold)
			te=LoadStart.load_ml(logdirpath+"\\"+subdir.getName()+".ml", te);
			int nextFold=te.getCurrentFold()+1;
			if (nextFold>10){
				te.write();
				continue;
			}
			for (int i=nextFold;i<11;i++){
				d11 = new Date();
				System.out.println(subdir.getName()+"\t"+nextFold);
				String trainpath=subdir.getAbsolutePath()+"\\"+i+"_train.arff";
				String testpath=subdir.getAbsolutePath()+"\\"+i+"_test.arff";
				te=Deal(trainpath,testpath,i,te,logdirpath+"\\"+subdir.getName());
				System.out.println("TimeCost:"+subdir.getName()+"("+i+")\t"+(float)(new Date().getTime()-d11.getTime())/3600+"minutes!");
			}
			te.write();
			System.out.println("TimeCost:"+subdir.getName()+"\t"+(float)(new Date().getTime()-d1.getTime())/3600+"minutes!");
		}//for
		System.out.println("TimeCost:ALL"+"\t"+(float)(new Date().getTime()-d0.getTime())/3600+"minutes!");
		
		new ExcelGather(inExcelDirPath,allExcelFilePath,outExcelGatherFilePath).run();
		
	}

}
