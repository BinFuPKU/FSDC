package lcRedo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import preprocess.Filter_;
import tools.InstancesFromFile;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class KMCluster_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//kmeans++
		String dir="C:\\Users\\fubin_pkusslabliu\\Desktop\\Áº³¬ÂÛÎÄ¸´ÏÖ\\data\\1-split\\";
		ArrayList<Integer> left;
		SimpleKMeans KM;
		for (int i=1;i<6;i++){
			String indir=dir+i+"\\0-arff\\";
			try {
				Instances train = InstancesFromFile.instancesFromARFF(indir+"train.arff");
				Instances test = InstancesFromFile.instancesFromARFF(indir+"test.arff");
				train.setClassIndex(train.numAttributes()-1);
				test.setClassIndex(test.numAttributes()-1);
				
				Instances newtrain = Filter_.remove(train, new int[]{train.numAttributes()-1});
//				System.out.println(i+"\t train(N*M):"+train.numInstances()+"\t"+train.numAttributes());
//				System.out.println(i+"\t newtrain(N*M):"+newtrain.numInstances()+"\t"+newtrain.numAttributes());
//				//
				Instances newTest = Filter_.remove(test, new int[]{test.numAttributes()-1});
//				System.out.println(i+"\t test(N*M):"+test.numInstances()+"\t"+test.numAttributes());
//				System.out.println(i+"\t newTest(N*M):"+newTest.numInstances()+"\t"+newTest.numAttributes());
//				
//				//			
				KM = new SimpleKMeans();
				String [] options={"-N",String.valueOf(3),"-init","1","-O"};//
				KM.setOptions(options);
				KM.buildClusterer(newtrain);
//
				int [] train_labels_int=KM.getAssignments();
				
				int [] test_labels_int=new int[newTest.numInstances()];
				for (int j=0;j<newTest.numInstances();j++)
					test_labels_int[j]=KM.clusterInstance(newTest.instance(j));
				
				out(train,train_labels_int,dir+i+"\\1-clusters\\","train");
				out(test,test_labels_int,dir+i+"\\1-clusters\\","test");

				System.gc();
				
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		
	}
	
	public static void out(Instances OriData,int [] label_int,String outdir,String t) throws IOException{
		///////////////
		HashMap<Integer,ArrayList<Integer>> hm=new HashMap<Integer,ArrayList<Integer>>();
		//train
		for (int j=0;j<label_int.length;j++){
			if (!hm.containsKey(label_int[j]))
				hm.put(label_int[j], new ArrayList<Integer>());
			hm.get(label_int[j]).add(j);
			//
		}
		//train
		Iterator it = hm.keySet().iterator();  
		while(it.hasNext()) {
			Integer key = (Integer)it.next();
			
			File od=new File(outdir+key+"\\data\\");
			if  (!od.exists())
				od.mkdirs();
			
			ArrayList<Integer> vals = hm.get(key);
			Instances fold_train = new Instances(OriData,vals.size());
			for(int j=0;j<vals.size();j++)
				fold_train.add(OriData.instance(vals.get(j)));
			 ArffSaver saver = new ArffSaver();
			 saver.setInstances(fold_train);
			 File file = new File(outdir+key+"\\data\\"+t+".arff");
			 file.createNewFile();
			 saver.setFile(file);
			 saver.writeBatch();

		}
	}

}
