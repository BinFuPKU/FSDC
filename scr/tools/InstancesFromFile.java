package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;

public class InstancesFromFile {

	//ARFF file,default ClassIndex==0
	public static Instances instancesFromARFF(String ARFFPath,int ClassIndex){
		BufferedReader br=null;
		FileReader fr=null;
		Instances instances=null;
		try {
			fr=new FileReader(ARFFPath);
			br=new BufferedReader(fr);
			instances=new Instances(br);
			instances.setClassIndex(ClassIndex);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (instances==null){
			try {
				throw new Exception("read nothing about ARFF file! "+ARFFPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instances;
	}

	public static Instances instancesFromARFF(String ARFFPath){
		BufferedReader br=null;
		FileReader fr=null;
		Instances instances=null;
		try {
			fr=new FileReader(ARFFPath);
			br=new BufferedReader(fr);
			instances=new Instances(br);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (instances==null){
			try {
				throw new Exception("read nothing about ARFF file! "+ARFFPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instances;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Instances iss = instancesFromARFF("D:\\data\\Roles_split_as_CF_\\train_dis_(rsac)_.arff",0);
		System.out.println(iss.numInstances());
	}

}
