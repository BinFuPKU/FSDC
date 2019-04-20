package cluster.K;

import java.util.ArrayList;

import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

public class HC_ {
	private HierarchicalClusterer hc = null;
	
	public HC_(){
		hc = new HierarchicalClusterer();
	}

	public String [] run(Instances train,int k) throws Exception{
		String [] options={"-N",String.valueOf(k)};//
		
		ArrayList<Integer> labels_int= new ArrayList<Integer>();
		hc.setOptions(options);
		hc.buildClusterer(train);
		for (int m=0;m<train.numInstances();m++){
			Instance in=train.instance(m);
			labels_int.add(hc.clusterInstance(in));
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
