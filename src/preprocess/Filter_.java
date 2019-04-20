package preprocess;

import java.util.ArrayList;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Filter_ {
	
	//filterWithClass :the last is the class
	public static Instances filterLeftClass(Instances its,ArrayList<Integer> left){//all include the classindex(col)
		Remove rm = new Remove();
		int all=its.numAttributes();
		int removeLen=all-(left.size()+1);
		if (removeLen==0){
			return its;
		}
		int [] removelist= new int [removeLen];
		
		int [] all_= new int[all-1];
		for (int i=0;i<all-1;i++)//all-1
			all_[i]=i;
		for(int i:left){//left
			all_[i]=-1;
		}
		int j=0;
		for (int i=0;i<all-1;i++){//(all-1)-left=remove
			if (all_[i]>0)
				removelist[j++]=all_[i];
		}
		
		rm.setAttributeIndicesArray(removelist);
		Instances newIts=null;
		try {
			rm.setInputFormat(its);
			newIts = Filter.useFilter(its, rm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("\t\tna:"+newIts.numAttributes());
		return newIts;
	}
	//filterWithClass
	public static Instances filterNotLeftClass(Instances its,ArrayList<Integer> left){//all include the classindex(col)
		Remove rm = new Remove();
		int all=its.numAttributes();
		int removeLen=all-left.size();
		if (removeLen==0){
			return its;
		}
		int [] removelist= new int [removeLen];
		
		int [] all_= new int[all];
		for (int i=0;i<all;i++)//all-1
			all_[i]=i;
		for(int i:left){//left
			all_[i]=-1;
		}
		int j=0;
		for (int i=0;i<all;i++){//(all-1)-left=remove
			if (all_[i]>0)
				removelist[j++]=all_[i];
		}
		
		rm.setAttributeIndicesArray(removelist);
		Instances newIts=null;
		try {
			rm.setInputFormat(its);
			newIts = Filter.useFilter(its, rm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("\t\tna:"+newIts.numAttributes());
		return newIts;
	}
		
	public static void main(String [] args){
//		String trainPath = "D:\\data\\Roles_split_as_CF_\\train_dis_(rsac)_.arff";
//		Instances train = InstancesFromFile.instancesFromARFF(trainPath, 0);
//		Remove rm = new Remove();
//		int [] removelist = {0,1,2,2199,2200};
//		rm.setAttributeIndicesArray(removelist);
//		Instances newIts=null;
//		System.out.println(train.size());
//		System.out.println(train.numAttributes());
//		try {
//			rm.setInputFormat(train);
//			newIts = Filter.useFilter(train, rm);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(newIts.numAttributes());
	}
}
