package excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ToExcel {
	
	private static int cv=10;
	private static int classfiers=3;
	
	//excel label: name
	private int numAttrsOrigin;
	
	private final static String [] heads1={"10-fold","num","select"};
	//ibk  nb j48 logistic rf smo mlp
	private final static String [] heads2={"10-fold","KNN(3)","NB","DT","LogisticR","RF","SMO","MLP"};
	

	private String outpath=null;
	private int currentFold;
	
	public int getCurrentFold() {
		return currentFold;
	}
	//10*1*[IBK,NB,J48]
	private ArrayList<Double[]> acc_select=null;
	private ArrayList<Double[]> auc_select=null;
	private ArrayList<Double[]> fm_select=null;
	
	private Double [] acc_ave=null;
	private Double [] auc_ave=null;
	private Double [] fm_ave=null;
	
	private Double [] acc_std=null;
	private Double [] auc_std=null;
	private Double [] fm_std=null;
	
	private ArrayList<ArrayList<Integer>> select=null;
	
	
	public int getNumAttrsOrigin() {
		return numAttrsOrigin;
	}
	public void setNumAttrsOrigin(int numAttrsOrigin) {
		this.numAttrsOrigin = numAttrsOrigin;
	}
	
	public ToExcel(String outpath){
		this.outpath=outpath;
		
		//double[]: [IBK,NB,J48]
		acc_select = new ArrayList<Double[]> ();
		auc_select = new ArrayList<Double[]> ();
		fm_select = new ArrayList<Double[]> ();
		
		acc_ave = new Double[this.classfiers];
		auc_ave = new Double[this.classfiers];
		fm_ave = new Double[this.classfiers];
		
		acc_std = new Double[this.classfiers];
		auc_std = new Double[this.classfiers];
		fm_std = new Double[this.classfiers];
		
		select = new ArrayList<ArrayList<Integer>> ();
		
		this.currentFold=0;
		this.numAttrsOrigin=0;
	}
	//3*3  select_results: [IBK,NB,J48]* [acc,auc,fm]
	public void add(ArrayList<Integer> sls,double [][] select_results){
		currentFold+=1;
		select.add(sls);
		for (int i=0;i<3;i++){
			Double [] temp1=new Double[3];
			Double [] temp2=new Double[3];			
			for (int j=0;j<this.classfiers;j++){
				temp2[j]=select_results[j][i];
			}
			if (i==0){
				acc_select.add(temp2);
			}
			else if(i==1){
				auc_select.add(temp2);
			}
			else if(i==2){
				fm_select.add(temp2);
			}
			else
				System.out.println("error in toExcel!");
		}
	}
	
	public void write(){
		calculate();
		WritableWorkbook wwb;
		try {
			wwb = Workbook.createWorkbook(new FileOutputStream(outpath));
			wwb=writeSelect(wwb);
			
			//1,2,3
			wwb=writeACCAUCFM(wwb,1);
			wwb=writeACCAUCFM(wwb,2);
			wwb=writeACCAUCFM(wwb,3);
			
			wwb.write();
            wwb.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//write select part (0)
	private WritableWorkbook writeSelect(WritableWorkbook wwb){
		try {
			//select
			WritableSheet sheet = wwb.createSheet("select", 0);
			int row=0;
			for (int i=0;i<heads1.length;i++){
				Label label =new Label(i,row,heads1[i]);
				sheet.addCell(label);
			}
			row+=2;
			double sum=0;
			for (int i=0;i<select.size();i++){
				//
				Label label1 =new Label(0,row,(i+1)+"");
				//
				Label label2 =new Label(1,row,(select.get(i).size())+"");
				//
				String val="";
				for (int j:select.get(i)){
					val+=j+",";
				}
				Label label3 =new Label(2,row,val.substring(0,val.length()-1));
				//add
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				sum+=select.get(i).size();
				row+=1;
			}
			sum/=10;
			{
				Label label=new Label(0,row,"AVE");
				sheet.addCell(label);
				label=new Label(1,row,sum+"");
				sheet.addCell(label);
				row+=1;
			}
			{
				Label label=new Label(0,row,"ALL");
				sheet.addCell(label);
				label=new Label(1,row,this.numAttrsOrigin+"");
				sheet.addCell(label);
			}
//			System.out.println("write!");
			
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wwb;
	}
	
	private void calculate(){
		//double[]: [IBK,NB,J48]
		
		//sum
		for (int i=0;i<this.cv;i++){
			for (int j=0;j<this.classfiers;j++){
				if (i==0){
					acc_ave[j]=(double) 0;
					auc_ave[j]=(double) 0;
					fm_ave[j]=(double) 0;
				}
				acc_ave[j]+=acc_select.get(i)[j];
				auc_ave[j]+=auc_select.get(i)[j];
				fm_ave[j]+=fm_select.get(i)[j];
			}
		}
		//ave
		for (int j=0;j<this.classfiers;j++){
			acc_ave[j]/=this.cv;
			auc_ave[j]/=this.cv;
			fm_ave[j]/=this.cv;
		}
		//1 sum of div
		for (int i=0;i<this.cv;i++){
			for (int j=0;j<this.classfiers;j++){
				if (i==0){
					acc_std[j]=(double) 0;
					auc_std[j]=(double) 0;
					fm_std[j]=(double) 0;
				}
				acc_std[j]+=(acc_select.get(i)[j]-acc_ave[j])*(acc_select.get(i)[j]-acc_ave[j]);
				auc_std[j]+=(auc_select.get(i)[j]-auc_ave[j])*(auc_select.get(i)[j]-auc_ave[j]);
				fm_std[j]+=(fm_select.get(i)[j]-fm_ave[j])*(fm_select.get(i)[j]-fm_ave[j]);
			}
		}
		//2 std
		for (int j=0;j<this.classfiers;j++){
			acc_std[j]=Math.sqrt(acc_std[j]/this.cv);
			auc_std[j]=Math.sqrt(auc_std[j]/this.cv);
			fm_std[j]=Math.sqrt(fm_std[j]/this.cv);
		}
		
	}
	
	// write acc part (1)
	private WritableWorkbook writeACCAUCFM(WritableWorkbook wwb,int n){
		ArrayList<Double[]> select_ =null;
		Double [] this_ave =null;
		Double [] this_std = null;
		String name=null;
		if (n==1){
			select_=acc_select;
			this_ave=acc_ave;
			this_std=acc_std;
			name="ACC";
		}
		else if(n==2){
			select_=auc_select;
			this_ave=auc_ave;
			this_std=auc_std;
			name="AUC";
		}
		else if(n==3){
			select_=fm_select;
			this_ave=fm_ave;
			this_std=fm_std;
			name="FM";
		}
		else
			System.out.println("error in the writeACCAUCFM1");
		
		try {
			//
			WritableSheet sheet = wwb.createSheet(name, n);
			Label label;
			int row=0;//head
			for (int i=0;i<heads2.length;i++){
				label =new Label(i,row,heads2[i]);
				sheet.addCell(label);
			}
			row+=1;
			label =new Label(0,row,"select:");
			sheet.addCell(label);
			row+=1;
			//select
			for (int i=0;i<select_.size();i++){
				label =new Label(0,row,(i+1)+"");
				sheet.addCell(label);
				for (int j=0;j<select_.get(i).length;j++){
					label =new Label(j+1,row,(select_.get(i)[j])+"");
					sheet.addCell(label);
				}
				row+=1;
			}
			//ave
			{
				label=new Label(0,row,"AVE");
				sheet.addCell(label);
				for (int i=0;i<this_ave.length;i++){
					label=new Label(i+1,row,this_ave[i]+"");
					sheet.addCell(label);
				}
				row+=1;
			}
			//std
			{
				label=new Label(0,row,"STD");
				sheet.addCell(label);
				for (int i=0;i<this_std.length;i++){
					label=new Label(i+1,row,this_std[i]+"");
					sheet.addCell(label);
				}
			}
			
			
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wwb;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ToExcel te= new ToExcel("I:\\FS\\libs\\test.xls");
		for (int i=0;i<10;i++){
			//double [][] all_results,ArrayList<Integer> sls,double [][] select_results
			te.setNumAttrsOrigin(20);
			ArrayList<Integer> sls= new ArrayList<Integer> ();
			for (int j=0;j<=i;j++)
				sls.add(j);
			double [][] select_results={{(double)i/100,(double)2*i/100,(double)3*i/100},
					{(double)4*i/100,(double)5*i/100,(double)6*i/100},
					{(double)7/100,(double)8*i/100,(double)9*i/100}};
			te.add(sls,select_results);
		}
		te.write();
	}

}
