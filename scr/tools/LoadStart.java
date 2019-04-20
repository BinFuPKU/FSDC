package tools;

import java.io.File;
import java.util.ArrayList;

import excel.ToExcel;

public class LoadStart {

	private static int classfiers=3;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	//获取log最后一行，开始恢复
	public static String load_log(String filepath){
		File file = new File(filepath);
		if (!file.exists())
			return null;
		String [] rs=IO.read(filepath).trim().split("\n");
		if (rs.length>0)
			return rs[rs.length-1];
		return null;
	}
	
	//ml file，开始恢复excel
	public static ToExcel load_ml(String filepath,ToExcel te){
		File file = new File(filepath);
		if (!file.exists())
			return te;
		String [] rs=IO.read(filepath).trim().split("\n");
		int start_line=rs.length;
		if (rs.length>0){
			int fold=Integer.parseInt(rs[rs.length-1].split("\t")[0]);
			start_line-=fold;
			for (int i=start_line;i<rs.length;i++){
				ArrayList<Integer> sls = new ArrayList<Integer>();
				double [][] select_results =new double [classfiers][3];
				
				String [] choose=rs[i].split("\t")[1].split(",");
				for (int j=0;j<choose.length;j++)
					sls.add(Integer.parseInt(choose[j]));
				
				String [] ml_results=rs[i].split("\t")[2].split(",");
				for (int j=0;j<ml_results.length;j++)
					select_results[j/3][j%3]=Double.parseDouble(ml_results[j]);
				
				te.add(sls, select_results);
			}
		}//if
		return te;
	}//load_toExcel
	
	public static void save_ml(String filepath,int currentFold,ArrayList<Integer> ints,double [][] select_results){
		String out=currentFold+"\t";
		for(int val:ints)
			out+=val+",";
		out=out.substring(0, out.length()-1)+"\t";
		for(int i=0;i<select_results.length;i++){
			for(int j=0;j<select_results[0].length;j++)
				out+=select_results[i][j]+",";
		}
		out=out.substring(0, out.length()-1)+"\r\n";
		IO.append(filepath, out);
	}
}
