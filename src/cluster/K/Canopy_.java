package cluster.K;

import java.util.ArrayList;

import weka.clusterers.Canopy;
import weka.core.Instance;
import weka.core.Instances;

public class Canopy_ {
	private Canopy canopy=null;
	
	public Canopy_(){
		canopy = new Canopy();
	}
	

	public String [] run(Instances train,int k) throws Exception{
		ArrayList<Integer> labels_int= new ArrayList<Integer>();
		
		String [] options={"-N",String.valueOf(k)};
		canopy.setOptions(options);
		canopy.buildClusterer(train);
		for (int m=0;m<train.numInstances();m++){
			Instance in=train.instance(m);
			labels_int.add(canopy.clusterInstance(in));
		}		
		//int[]2String[]
		String [] labels_str = new String[labels_int.size()];
		for (int i=0;i<labels_str.length;i++){
			labels_str[i]=String.valueOf(labels_int.get(i));
//			System.out.print(labels_int.get(i)+"\t");
		}
		return labels_str;
	}
}
