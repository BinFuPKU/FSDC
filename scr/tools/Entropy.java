package tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

public class Entropy {
	private static DecimalFormat df   = new DecimalFormat("######0.000000");

	//Entropy
	private static double run(String [] labels){
		HashMap<String, Integer> hm_label=new HashMap<String, Integer>();
		double size_=labels.length;
		for(int i=0;i<size_;i++){
			hm_label.put(labels[i], hm_label.getOrDefault(labels[i], 0)+1);
		}
		double entropy=0;
		Iterator iter = hm_label.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			String key = (String) entry.getKey();
			int val = (Integer) entry.getValue();
			entropy+=(val/size_)*Math.log(val/size_);
		}
		return Double.parseDouble(df.format(entropy));
	}
}
