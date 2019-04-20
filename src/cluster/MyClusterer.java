package cluster;

import weka.core.Instances;
import cluster.K.Canopy_;
import cluster.K.EM_;
import cluster.K.FarthestFirst_;
import cluster.K.HC_;
import cluster.K.KMeansCanopy_;
import cluster.K.KMeansFF_;
import cluster.K.KMeansPP_;
import cluster.K.KMeans_;
import cluster.K.MakeDensityBasedClusterer_;

public class MyClusterer {
	public final static String clusterers="em/ff/hc/mdbc/canopy/kmeans/kpp/kc/kff";
	private String which=null;
	
	private EM_ em_=null;
	private FarthestFirst_ ff_=null;
	private HC_ hc_=null;
	private MakeDensityBasedClusterer_ mdbc_=null;

	private KMeans_ kmeans_=null;
	private Canopy_ canopy_=null;
	private KMeansPP_ kmeanspp_=null;
	private KMeansCanopy_ kmeanscanopy_=null;
	private KMeansFF_ kmeansff_=null;
	
	public MyClusterer(String which){
		this.which=which;
		
		if (which.equalsIgnoreCase("em"))
			em_=new EM_();
		else if (which.equalsIgnoreCase("ff"))
			ff_=new FarthestFirst_();
		else if (which.equalsIgnoreCase("hc"))
			hc_=new HC_();
		else if (which.equalsIgnoreCase("mdbc"))
			mdbc_=new MakeDensityBasedClusterer_();
		
		else if (which.equalsIgnoreCase("canopy"))
			canopy_=new Canopy_();
		else if (which.equalsIgnoreCase("kmeans"))
			kmeans_=new KMeans_();
		else if (which.equalsIgnoreCase("kpp"))
			kmeanspp_=new KMeansPP_();
		else if (which.equalsIgnoreCase("kc"))
			kmeanscanopy_=new KMeansCanopy_();
		else if (which.equalsIgnoreCase("kff"))
			kmeansff_=new KMeansFF_();
		else{
		System.out.println(which.length());
		System.out.println("init:cluster choose wrong("+which+")!");
		System.exit(1);
		}
	}
	
	public String [] run(Instances train,int k) throws Exception{
		
		if (which.equalsIgnoreCase("em"))
			return em_.run(train, k);
		else if (which.equalsIgnoreCase("ff"))
			return ff_.run(train, k);
		else if (which.equalsIgnoreCase("hc"))
			return hc_.run(train, k);
		else if (which.equalsIgnoreCase("mdbc"))
			return mdbc_.run(train, k);
		else if (which.equalsIgnoreCase("canopy"))
			return canopy_.run(train, k);
		else if (which.equalsIgnoreCase("kmeans"))
			return kmeans_.run(train, k);
		else if (which.equalsIgnoreCase("kpp"))
			return kmeanspp_.run(train, k);
		else if (which.equalsIgnoreCase("kc"))
			return kmeanscanopy_.run(train, k);
		else if (which.equalsIgnoreCase("kff"))
			return kmeansff_.run(train, k);
		else{
		System.out.println("run:cluster choose wrong("+which+")!");
		System.exit(1);
		return null;
		}
		
	}
}
