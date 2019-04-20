package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import tools.IO;

public class ExcelGather {
	//Only for ACC

	private static int numDataset=10;
	private static int classfiers=3;
	
	private static String inDirPath=null;
	private static String outFilePath=null;
	private static String allFilePath=null;//"I:/FS/libs/result(all).txt";
	private static String [] heads={"cluster","length","delta","","CTG","imageSeg","ionosphere","krvskpEW","page-blocks","penbased",
		"satimage","texture","waveformEW","wdbc"};
	//base on all dataset
	//10*3:numDataset*classfiers
	private static float all_results [][]=null;
	private static int [] numAttrsAll=null;
	
	private float select_results [][]=null;
	private float [] numAttrsSelect=null;
	
	private static String cluster=null;
	private static String length=null;
	private static String delta=null;
	
	public ExcelGather(String inDirPath,String allFilePath,String outFilePath){
		this.inDirPath=inDirPath;
		this.allFilePath=allFilePath;
		this.outFilePath=outFilePath;
		if (all_results==null || numAttrsAll==null){
			load_allFile();
		}
		
		//parameters
		System.out.println(inDirPath);
		String [] pathParas = inDirPath.split("/");
		String [] items = pathParas[pathParas.length-1].split("_");
		cluster = items[0];
		length = items[2];
		delta = items[1];
	}
	//run
	public void run(){
		if (select_results==null)
			select_results=new float[numDataset][classfiers];
		if (numAttrsSelect==null)
			numAttrsSelect=new float[numDataset];
		excel_gather();
		add2Excel();
	}
	
	//result(all).txt
	private static void load_allFile(){
		all_results = new float[numDataset][classfiers];
		numAttrsAll = new int[numDataset];
		String [] paras=IO.read(allFilePath).split("\n\n");
//		System.out.println(paras.length);
		for (int i=0;i<paras.length;i++){
			String [] items=paras[i].split("\n")[1].split("\t");
			for(int j=0;j<classfiers;j++){
				all_results[i][j]=Float.parseFloat(items[j+1].replace("%", "").trim())/100;
			}
//			System.out.println((i)+"\t"+all_results[i][0]+"\t"+all_results[i][1]+"\t"+all_results[i][2]);
			numAttrsAll[i]=Integer.parseInt(paras[i].split("\n")[0].split("\t")[1]);
		}
	}
	
	
	//excel gather
	private void excel_gather(){
	    InputStream stream = null;
	    Workbook rwb = null;
	    Sheet sheet = null;
	    
	    for (int i=4;i<14;i++){
	    	String inExcelPath = inDirPath+"/"+heads[i]+"-10-fold.xls";
	    	//open
	    	try {
				stream = new FileInputStream(inExcelPath);
		    	rwb = Workbook.getWorkbook(stream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	//select
	    	sheet = rwb.getSheet(0);
//	    	System.out.println(sheet.getRows()+"\t"+sheet.getColumns());
	    	numAttrsSelect[i-4] = Float.parseFloat(sheet.getCell(1,12).getContents());    
	    	
	    	//ACC
	    	sheet = rwb.getSheet(1);
	    	select_results[i-4][0]=Float.parseFloat(sheet.getCell(1,12).getContents());
	    	select_results[i-4][1]=Float.parseFloat(sheet.getCell(2,12).getContents());
	    	select_results[i-4][2]=Float.parseFloat(sheet.getCell(3,12).getContents());

//			System.out.println((i-4)+"\t"+select_results[i-4][0]+"\t"+select_results[i-4][1]+"\t"+select_results[i-4][2]);
	    	//close
	    	try {
		    	rwb.close();
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	//add excel
	private void add2Excel(){
		boolean fileExist=true;
		File file = new File(outFilePath);
		if (!file.exists()){
			fileExist=false;
		}
		
		Workbook book = null;
		Sheet sheet = null;
		WritableWorkbook wbook = null;;
		WritableSheet wsheet0,wsheet1,wsheet2,wsheet3=null;
		int row=0;
		
		
		try {
			if (file.exists()){
				book = Workbook.getWorkbook(file);
				wbook = Workbook.createWorkbook(file, book);
			}
			else{
				wbook = Workbook.createWorkbook(file);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//select
		if (!fileExist){
			wsheet0 = wbook.createSheet("select", 0);
			wsheet1 = wbook.createSheet("IBK", 1);
			wsheet2 = wbook.createSheet("NB", 2);
			wsheet3 = wbook.createSheet("J48", 3);
		}
		else{
			sheet = book.getSheet(0);
			row = sheet.getRows();
			wsheet0 = wbook.getSheet(0);
			wsheet1 = wbook.getSheet(1);
			wsheet2 = wbook.getSheet(2);
			wsheet3 = wbook.getSheet(3);
		}
		System.out.println("ExcelStart:"+row);
		
		try {
			wsheet0=toWritableSheet(wsheet0,row,0);
			wsheet1=toWritableSheet(wsheet1,row,1);
			wsheet2=toWritableSheet(wsheet2,row,2);
			wsheet3=toWritableSheet(wsheet3,row,3);

			wbook.write();
			wbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private WritableSheet toWritableSheet(WritableSheet wsheet,int row,int j) throws RowsExceededException, WriteException{
		Label label=null;
		//head
		if (row==0){
			for (int c=0;c<heads.length;c++){
				label = new Label(c, row, heads[c]);
				wsheet.addCell(label);
			}
			row+=1;
		}
		for(int i= 0;i< heads.length;i++) {//content
			if (i==0){
				label = new Label(i, row, cluster);
				wsheet.addCell(label);
			}
			else if(i==1){
				label = new Label(i, row, length);
				wsheet.addCell(label);
			}
			else if(i==2){
				label = new Label(i, row, delta);
				wsheet.addCell(label);
			}
			else if(i>3){
				if (j==0)
					label = new Label(i, row, numAttrsSelect[i-4]+"");
				else
					label = new Label(i, row, (select_results[i-4][j-1]-all_results[i-4][j-1])+"");
				wsheet.addCell(label);
			}
		}//for
		
		return wsheet;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		Float.parseFloat("56.23");
//		load_allFile();
//		for (int i=0;i<10;i++){
//			for(int j=0;j<classfiers;j++)
//				System.out.println("val("+i+","+j+")="+all_results[i][j]);
//			System.out.println("numAttrs("+i+")="+numAttrsAll[i]);
//		}
		ExcelGather eg = new ExcelGather("I:/FS/mdbc/excel/mdbc_0.4_logN","I:/FS/libs/result(all).txt" ,"I:/FS/libs/test_.xls");
		eg.run();
		eg = new ExcelGather("I:/FS/mdbc/excel/mdbc_0.4_logN","I:/FS/libs/result(all).txt" ,"I:/FS/libs/test_.xls");
		eg.run();
		
		
	}
	

}
