package cluster.K;

import java.util.ArrayList;

import weka.clusterers.MakeDensityBasedClusterer;
import weka.core.Instance;
import weka.core.Instances;

public class MakeDensityBasedClusterer_ {
	private MakeDensityBasedClusterer mdbc= null;
	
	public MakeDensityBasedClusterer_(){
		mdbc =new MakeDensityBasedClusterer();
	}

	public String [] run(Instances train,int k) throws Exception{
		ArrayList<Integer> labels_int= new ArrayList<Integer>();
		
		String [] options={"-N",String.valueOf(k)};
		mdbc.setOptions(options);
		mdbc.buildClusterer(train);
		for (int m=0;m<train.numInstances();m++){
			Instance in=train.instance(m);
			labels_int.add(mdbc.clusterInstance(in));
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
