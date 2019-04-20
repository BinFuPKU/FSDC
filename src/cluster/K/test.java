package cluster.K;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cluster.MyClusterer;
import preprocess.Filter_;
import weka.clusterers.*;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		KMeansPP_ kpp = new KMeansPP_();
		
//		MyClusterer mc=new MyClusterer("mdbc");
		
		File file= new File("I:\\TempFiles\\10_\\CTG-10-fold\\1_train.arff");
        ArffLoader loader = new ArffLoader();
        try {
			loader.setFile(file);
	        Instances ins = loader.getDataSet();
	        System.out.println(ins.numAttributes());
	        ins.setClassIndex(ins.numAttributes()-1);
	        System.out.println(ins.numAttributes());
	        
//	        for (int i=0;i<ins.numInstances();i++)
//	        	System.out.println(ins.instance(i).toString());;
//	        
	        ArrayList<Integer> left=new ArrayList<Integer>();
	        left.add(1);left.add(2);left.add(3);left.add(4);
	        
	        HashMap<String,Integer> c1=new  HashMap<String,Integer>();
	        HashMap<String,Integer> c2=new  HashMap<String,Integer>();
	        
	        ins=Filter_.filterNotLeftClass(ins, left);
	        //CobWeb_
	        //DBSCAN_
	        String [] rs1=kpp.run(ins, 2);
	        String [] rs2=kpp.run(ins, 10);
	        System.out.println(0);
	        for (int i=0;i<rs1.length;i++){
	        	c1.put(rs1[i], c1.getOrDefault(rs1[i], 0)+1);
	        	c2.put(rs2[i], c2.getOrDefault(rs2[i], 0)+1);
//	        	System.out.println(rs1[i]+"\t"+rs2[i]);
	        }
	        Iterator iter =(Iterator) c1.entrySet().iterator();
	        while(iter.hasNext()){
	        	Map.Entry entry = (Map.Entry) iter.next();
	        	Object key = entry.getKey();
	        	Object val = entry.getValue();
	        	System.out.println(1+"\t"+key+"\t"+val);
	        }
	        System.out.println("01");
	        iter =(Iterator) c2.entrySet().iterator();
	        while(iter.hasNext()){
	        	Map.Entry entry = (Map.Entry) iter.next();
	        	Object key = entry.getKey();
	        	Object val = entry.getValue();
	        	System.out.println(2+"\t"+key+"\t"+val);
	        }
	        
//	        for (Instance in:ins)
//	        	System.out.println(in.stringValue(ins.numAttributes()-1));
//	        System.out.println(ins.numAttributes()+"\t"+"\t"+ins.numInstances()+"\t"+ins.numClasses());
//	        Canopy(ins,3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
