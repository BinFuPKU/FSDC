package cluster.K;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class KMeansPP_ {
	private SimpleKMeans KM= null;
	
	public KMeansPP_(){
		KM = new SimpleKMeans();
	}
	
	public String [] run (Instances train,int k) throws Exception {
		String [] options={"-N",String.valueOf(k),"-init","1","-O"};//
		
		int [] labels_int=null;
			KM.setOptions(options);
			KM.buildClusterer(train);
			labels_int=KM.getAssignments();
			
		//String 
		String [] labels_str = new String[labels_int.length];
		for (int i=0;i<labels_str.length;i++)
			labels_str[i]=String.valueOf(labels_int[i]);
		return labels_str;
	}
	
}
