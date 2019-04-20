package excel;

import java.util.ArrayList;

public class BiTuples {
	private ArrayList<Integer> ints;
	private ArrayList<Double> doubles;
	
	public BiTuples(){
		ints = new  ArrayList<Integer>();
		doubles = new  ArrayList<Double>();
	}
	
	public void add(int col, double mi){
		ints.add(col);
		doubles.add(mi);
	}
	public ArrayList<Integer> getSelect(){
		return ints;
	}
	public String toString(){
		String str="";
		for (int i=0;i<ints.size();i++){
			str+=ints.get(i)+"\t"+doubles.get(i)+"\n";
		}
		return str;
	}

}
