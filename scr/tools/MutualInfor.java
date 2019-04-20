package tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

public class MutualInfor {
	private static DecimalFormat df   = new DecimalFormat("######0.000000");

	public static double run(String [] targets, String [] labels){
		if (targets.length!=labels.length){
			System.out.println("len(targets)!=len(labels)");
			System.exit(1);
		}
		HashMap<String, Integer> hm_target_label=new HashMap<String, Integer>();
		HashMap<String, Integer> hm_target=new HashMap<String, Integer>();
		HashMap<String, Integer> hm_label=new HashMap<String, Integer>();
		double size_=(double)targets.length;
		for(int i=0;i<size_;i++){
			String key=targets[i]+"_"+labels[i];
			hm_target_label.put(key, hm_target_label.getOrDefault(key,0)+1);
			hm_target.put(targets[i], hm_target.getOrDefault(targets[i], 0)+1);
			hm_label.put(labels[i], hm_label.getOrDefault(labels[i], 0)+1);
		}
		double mi=0;
		
		Iterator iter = hm_target_label.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String key = (String) entry.getKey();
			int val = (Integer) entry.getValue();
			String [] snames=key.split("_");
//			System.out.println("\t\t"+hm_target.get(snames[0])/size_);
			mi+=(val/size_)*Math.log((val/size_)/((hm_target.get(snames[0])/size_)*(hm_label.get(snames[1])/size_)));
		}
//		System.out.println(mi);
		return Double.parseDouble(df.format(mi));
	
	}
}
