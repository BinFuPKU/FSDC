package cluster.K;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class KMeans_ {
	private SimpleKMeans KM= null;
	
	public KMeans_(){
		KM = new SimpleKMeans();
	}
	
	public String [] run (Instances train,int k) throws Exception {
		String [] options={"-N",String.valueOf(k),"-init","0","-O"};//
		
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
