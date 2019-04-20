package select;

import cluster.MyClusterer;
import tools.IO;
import tools.MutualInfor;
import weka.core.Instances;

public class SelectK {
	private static MyClusterer mc=null;
	private static double delta_t;
	private static int window_len;

	public SelectK(double delta_t,String cluster){
		this.delta_t=delta_t;
		this.mc=new MyClusterer(cluster);
	}
	
	public void setWindow_len(int window_len) {
		this.window_len = window_len;
	}


	// find the last efficient result
	private int binaryFindEnd(Instances train,String [] labels,int numClasses){
//			System.out.println("binaryFindEnd:");
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
	public double [] maxMI(Instances train,String [] labels,int numClasses,String path) throws Exception{
//			System.out.println("maxMI:");
		double [] rs=new double[2];
		int maxK=0;
		double maxMI=0;
		// numClasses->train.size()
		int start=(numClasses>2?2:numClasses);//(int)Math.sqrt(train.size());
		int end=binaryFindEnd(train,labels,numClasses);
		double pre_mi=0;
		System.out.println("\t\t\tSearchBestKStart(start,end)=("+start+","+end+")");
//			double [] window_logn=new double[(int)Math.log(end)];->window
		double [] window= new double[window_len];
		int i_log=0;
		boolean logn_flag=false;
		int i=start;
		for (;i<=end;i++){
			String [] targets;
			try{
				targets=mc.run(train,i);//
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			
			double mi = MutualInfor.run(targets,labels);///Math.log(i);//((Entropy(targets)+Entropy(labels))/i);
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
}
