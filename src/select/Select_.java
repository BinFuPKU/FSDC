package select;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import preprocess.Filter_;
import cluster.MyClusterer;
import cluster.K.MakeDensityBasedClusterer_;
import excel.BiTuples;
import tools.IO;
import weka.clusterers.AbstractClusterer;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Select_ {
	private static DecimalFormat df   = new DecimalFormat("######0.000000");
//	private static double skip_margin = 0.05;
	private static double delta_t;
	private static int window_len;
	private static MyClusterer mc=null;
	
	public Select_(double delta_t, String cluster){
		this.delta_t=delta_t;
		this.mc=new MyClusterer(cluster);
	}

	public static void setWindow_len(int window_len) {
		Select_.window_len = window_len;
	}



	//Entropy
	private  double Entropy(String [] labels){
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
	
	//mutualInfor
	private  double mutualInfor(String [] targets, String [] labels){
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
	// find the last efficient result
	private int binaryFindEnd(Instances train,String [] labels,int numClasses){
//		System.out.println("binaryFindEnd:");
		boolean flag=false;
		int start=(numClasses>2?2:numClasses);//(int)Math.sqrt(train.size());
		int end=train.numInstances();
		int mid=(start+end)/2;
		try {
			mc.run(train,end);
		} catch (Exception e) {
			flag=true;
		}

		while (flag && start<end-1){
			try {
				mc.run(train,mid);
				start=mid;
				mid=(start+end)/2;
			} catch (Exception e) {
				end=mid;
				mid=(start+end)/2;
			}
		}
		if (flag)
			return mid;
		else
			return end;
	}
	private double [] maxMI(Instances train,String [] labels,int numClasses,String path) throws Exception{
//		System.out.println("maxMI:");
		double [] rs=new double[2];
		int maxK=0;
		double maxMI=0;
		// numClasses->train.size()
		int start=(numClasses>2?2:numClasses);//(int)Math.sqrt(train.size());
		int end=binaryFindEnd(train,labels,numClasses);
		double pre_mi=0;
		System.out.println("\t\t\tSearchBestKStart(start,end)=("+start+","+end+")");
//		double [] window_logn=new double[(int)Math.log(end)];->window
		double [] window= new double[window_len];
		int i_log=0;
		boolean logn_flag=false;
		int i=start;
		for (;i<=end;i++){
			String [] targets=mc.run(train,i);//
			double mi = mutualInfor(targets,labels);///Math.log(i);//((Entropy(targets)+Entropy(labels))/i);
			if (mi>maxMI){
				maxMI=mi;
				maxK=i;
			}
			if (i!=start){
				window[i_log++]=(mi-pre_mi)/mi;
				if (i_log>=window.length){
					i_log=i_log%window_len;
					logn_flag=true;
				}
			}
			//check
			boolean break_flag=false;
			if (logn_flag){
				int m=0;
				while(m<window.length && window[m]<=delta_t)
					m+=1;
				if (m>=window.length)
					break_flag=true;
			}
			//
			if (break_flag)
				break;
			pre_mi=mi;
		}
		System.out.println("\t\t\tSearchBestKEnd:(maxK,MaxSMI)=("+maxK+","+maxMI+")");
		IO.append(path,"\t\t\tMaxSMI("+"maxK,maxMI)= ("+maxK+","+maxMI+") end:("+i+")\r\n" );
		rs[0]=maxK;
		rs[1]=maxMI;
		return rs;
	}
	
	private Double [] select(Instances train,int currentFold, ArrayList<Integer> choose, ArrayList<Integer> left, String path){
//		System.out.println("select:");
		int na=train.numAttributes()-1;
		double maxMI=0;
		int maxI=0;//index of left
		
		for (int i=0;i<left.size();i++){//遍历：依次尝试添加
			System.out.println("\t\tTryStart:"+left.get(i)+"");
			choose.add(left.get(i));
			
			int numClasses=train.numClasses();
			String [] labels=new String[train.numInstances()];
			for (int k=0;k<labels.length;k++)
				labels[k]=(String)train.instance(k).stringValue(train.numAttributes()-1);
			//
			Instances newtrain=Filter_.filterNotLeftClass(train,choose);
			//聚类
			//互信息
			double maxMI_k=0;
			double[] rss=new double[2];
			try {
				rss = maxMI(newtrain,labels,numClasses,path);
				IO.append(path, currentFold+"\tTry:(i,k,MI)=("+left.get(i)+","+(int)rss[0]+","+rss[1]+")\r\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("terrible!");
				System.exit(1);
			}
			maxMI_k=rss[1];
			System.out.println("\t\tTryEnd:(k,MI)=("+(int)rss[0]+","+rss[1]+")");
			//比较 
			if (maxMI_k>maxMI){
				maxMI=maxMI_k;
				maxI=i;//index of left
			}
			choose.remove(choose.size()-1);//恢复
		}
		//first is index in left;second is the mi
		Double[] rs= new Double[2];
		rs[0]=(double)(maxI);//index of left
		rs[1]=maxMI;
		return rs;
	}

	public BiTuples deal(Instances train,int currentFold, String path){
		BiTuples rs_tt = new BiTuples();
		ArrayList<Integer> choose=new ArrayList<Integer>();
		ArrayList<Integer> left=new ArrayList<Integer>();
		
		for (int i=0;i<train.numAttributes()-1;i++)
			left.add(i);
		
		double preMaxMI=0;
		boolean flag=true;
		while(flag && left.size()!=0){
			System.out.println("\tAddAttriStart:(choose,left)=("+choose.size()+","+left.size()+")");
			Double [] rs=select(train,currentFold,choose,left,path+".details");
//			System.out.println("\twhile2:"+choose.size()+"\t"+left.size());
			int index=rs[0].intValue();
			double mi=rs[1];
			if(mi>preMaxMI){
				preMaxMI=mi;
				int col=left.remove(index);//index of left
				choose.add(col);
				rs_tt.add(col, mi);
//				System.out.println("\t\t"+mi+">"+preMaxMI  so continue!");
			}
			else
				flag=false;
			String s="[";
			for (int k:choose)
				s+=k+",";
			String l="[";
			for (int k:left)
				l+=k+",";
			System.out.println("\t");
			System.out.println("\tAddAttriEnd:(choose,left)=("+choose.size()+","+left.size()+")=("+
				s.substring(0, s.length()-1)+"],"+l.substring(0, l.length()-1)+"])");

			//write _log
			String out=currentFold+"\t";
			for(int i:choose)
				out+=i+",";
			out=out.substring(0, out.length()-1)+"\t";
			for(int i:left)
				out+=i+",";
			out=out.substring(0, out.length()-1)+"\t"+mi+"\r\n";
			IO.append(path+".log", out);
		}
//		System.out.println("end MaxMI:"+preMaxMI);
		return rs_tt;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		File file= new File("C:\\Users\\fubin\\10_\\CTG-10-fold\\1_train.arff");
//        ArffLoader loader = new ArffLoader();
//        try {
//			loader.setFile(file);
//	        Instances ins = loader.getDataSet();
//	        ins.setClassIndex(ins.numAttributes()-1);
////	        System.out.println(ins.numClasses());
////	        for (Instance in:ins)
////	        	System.out.println(in.stringValue(ins.numAttributes()-1));
//	        ArrayList<Integer> rs=deal(ins);
//	        System.out.print("result:");
//	        for (int i=0;i<rs.size();i++)
//	        	System.out.print((rs.get(i)+1)+",");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String indirpath="D:\\聚类论文\\源代码\\10_";
		String outdirpath="D:\\聚类论文\\源代码\\result\\excel\\k-means_sqrt(n)_n";
		
		Select_ select = new Select_(0.5,"mdbc");
		File outdir=new File(outdirpath);
		if  (!outdir.exists())
			outdir.mkdir(); 
//		boolean flag=true;
		for (File subdir:new File(indirpath).listFiles()){
			System.out.println(subdir.getName());
//			if (subdir.getName().equals("waveformEW-10-fold"))
//				flag=false;
//			if (flag)
//				continue;
//			if (!subdir.getName().equals("CTG-10-fold"))
//				continue;
			for (int i=1;i<11;i++){
				if (i!=2)
					continue;
				String trainpath=subdir.getAbsolutePath()+"\\"+i+"_train.arff";
				File file= new File(trainpath);
				 ArffLoader loader = new ArffLoader();
		        try {
					loader.setFile(file);
			        Instances ins = loader.getDataSet();
			        ins.setClassIndex(ins.numAttributes()-1);
			        
			        select.setWindow_len((int)Math.log(ins.numInstances()));
//			        System.out.println(ins.numClasses());
//			        for (Instance in:ins)
//			        	System.out.println(in.stringValue(ins.numAttributes()-1));
			        BiTuples rs_tt=select.deal(ins,i,outdirpath+"\\"+subdir.getName()+"_"+i+"_");
			        IO.append(outdirpath+"\\"+i+".txt", rs_tt.toString());
		        	}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		        		}
			
			}//for i
		}// for dir

	}//main
}