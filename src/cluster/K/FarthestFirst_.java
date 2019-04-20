package cluster.K;

import java.util.ArrayList;

import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.core.Instance;
import weka.core.Instances;

public class FarthestFirst_ {
	private FarthestFirst ff = null;
	
	public FarthestFirst_(){
		ff = new FarthestFirst();
	}
	
	public String [] run(Instances train,int k) throws Exception{
		String [] options={"-N",String.valueOf(k)};//
//		MakeDensityBasedClusterer mdbc= new MakeDensityBasedClusterer();
		
		ArrayList<Integer> labels_int= new ArrayList<Integer>();
		ff.setOptions(options);
		ff.buildClusterer(train);
		for (int m=0;m<train.numInstances();m++){
			Instance in=train.instance(m);
			labels_int.add(ff.clusterInstance(in));
		}		
		//String 
		String [] labels_str = new String[labels_int.size()];
		for (int i=0;i<labels_str.length;i++){
			labels_str[i]=String.valueOf(labels_int.get(i));
//			System.out.print(labels_int.get(i)+"\t");
		}
		return labels_str;
	}
}
