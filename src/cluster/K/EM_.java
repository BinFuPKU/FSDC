package cluster.K;

import java.util.ArrayList;

import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;

public class EM_ {
	private EM em =null;	
	
	public EM_(){
		em = new EM();
	}
	
	public String [] run(Instances train,int k) throws Exception{
		String [] options={"-N",String.valueOf(k)};//
		
		ArrayList<Integer> labels_int= new ArrayList<Integer>();
		em.setOptions(options);
		em.buildClusterer(train);
		for (int m=0;m<train.numInstances();m++){
			Instance in=train.instance(m);
			labels_int.add(em.clusterInstance(in));
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
