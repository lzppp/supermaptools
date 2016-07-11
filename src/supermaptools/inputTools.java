package supermaptools;

import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;  
import org.apache.poi.hssf.usermodel.HSSFCell;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoPoint;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.Recordset.BatchEditor;
import com.supermap.data.Workspace; 

public class inputTools {
	private static int lastrow=0;
	private static toadd[] points=new toadd[656];
	public static void main(String[] args) {
		//读取excel
		//init();
		readXml("./get_poi.xls");
		//openDB();
		pt();
	}
	private static void init() {
		// TODO Auto-generated method stub
		for (int i=0;i<1382;i++){
			
		}
		
	}
	private static void pt(){
		for (int i =0;i<points.length;i++){
			//toadd apoint = (toadd)iterator.next(); 
			//System.out.println(apoint.ID);
			System.out.println(points[i].ID);
			System.out.println(points[i].Point.getX());

		}
		
	}
	    private static void openDB() {
	        // 定义工作空间
	        Workspace workspace = new Workspace();
	        Datasources datasources = workspace.getDatasources();
	        // 定义数据源连接信息，假设以下所有数据源设置都存在
	        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo(
					"./my.udb", "test", "");
	        
			//datasourceConnectionInfo.setEngineType(EngineType.UDB);
			if (datasourceConnectionInfo != null){
				System.out.println(datasourceConnectionInfo.toString());
			}
			
			Datasource datasource = datasources.open(datasourceConnectionInfo);
			if (datasource == null) {
                System.out.println("打开数据源失败");
                return;
			} else {
                System.out.println("数据源打开成功！");
			}
			Datasets datasets = datasource.getDatasets();
			Dataset dataset = datasets.get("New_Point");
			if (dataset==null)System.out.println("打开数据源失败");
			DatasetVector datasetVector = (DatasetVector) dataset;

			//得到数据集的记录集
			Recordset recordset = datasetVector.getRecordset(false,
					CursorType.DYNAMIC);
			//Recordset recordset = datasetVector.getRecordset(true,CursorType.DYNAMIC);
			if (recordset == null) {
                System.out.println("打开record失败");
                return;
			} else {
                System.out.println("record打开成功！");
			}

	        // 设置批量更新每次提交的记录数目
	        //editor.setMaxRecordCount(lastrow-1);
	        // 从 World 数据集中读取几何对象和字段值，批量更新到 example 数据集中
			BatchEditor editor = recordset.getBatch();
			editor.begin();
			for (int i =0;i<points.length;i++){
				//toadd apoint = (toadd)iterator.next(); 
				//System.out.println(apoint.ID);
				java.util.Map map = new java.util.HashMap();
				map.put("STName_1",points[i].ID);
				FieldInfos fieldInfos = recordset.getFieldInfos();
				
				recordset.addNew(points[i].Point, map);
				//iterator.next();

			}
			
			
	        // 批量操作统一提交
	        editor.update();

	        // 释放记录集
	        recordset.dispose();
	        
	        
	        // 释放工作空间资源
	        datasourceConnectionInfo.dispose();
	        workspace.dispose();

	    }
		public static void readXml(String fileName){  
	        boolean isE2007 = false;    //判断是否是excel2007格式  
	        if(fileName.endsWith("xlsx"))  
	            isE2007 = true;  
	        try {  
	            InputStream input = new FileInputStream(fileName);  //建立输入流  
	            Workbook wb  = null;  
	            //根据文件格式(2003或者2007)来初始化  
	            if(isE2007)  
	                wb = new XSSFWorkbook(input);  
	            else  
	                wb = new HSSFWorkbook(input);  
	            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单  
	            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器  
	            while (rows.hasNext()) {
	                Row row = rows.next();  //获得行数据 
	                if (row.getRowNum()==0)
	            		continue;
	                System.out.println(row.getRowNum());
	                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
	                String toAddC = null;
	                double toAddX = 0;
	                double toAddY = 0;
	                while (cells.hasNext()) { 
	                	Cell cell = cells.next();
	                	if(cell.getColumnIndex()==4){
		                    //System.out.println("Cell #" + cell.getColumnIndex());  
			                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据  
			                    case HSSFCell.CELL_TYPE_NUMERIC: 
			                        break;  
			                    case HSSFCell.CELL_TYPE_STRING:  
			                        //System.out.println(cell.getStringCellValue());
			                        toAddC=cell.getStringCellValue();
			                        System.out.println(cell.getStringCellValue());
			                        break;  
			                    case HSSFCell.CELL_TYPE_BOOLEAN:  
			                        System.out.println(cell.getBooleanCellValue());  
			                        break;  
			                    case HSSFCell.CELL_TYPE_FORMULA:  
			                        System.out.println(cell.getCellFormula());  
			                        break;  
			                    default:  
			                        System.out.println("unsuported sell type");  
			                    break;  
			                    }
		                	}
	                	if(cell.getColumnIndex()==2){
	                    //System.out.println("Cell #" + cell.getColumnIndex());  
		                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据  
		                    case HSSFCell.CELL_TYPE_NUMERIC:
		                    	toAddX=cell.getNumericCellValue();
		                        //System.out.println(cell.getNumericCellValue());  
		                        break;  
		                    case HSSFCell.CELL_TYPE_STRING:  
		                        System.out.println(cell.getStringCellValue()); 
		                        toAddX= Float.parseFloat(cell.getStringCellValue());
		                        break;  
		                    case HSSFCell.CELL_TYPE_BOOLEAN:  
		                        System.out.println(cell.getBooleanCellValue());  
		                        break;  
		                    case HSSFCell.CELL_TYPE_FORMULA:  
		                        System.out.println(cell.getCellFormula());  
		                        break;  
		                    default:  
		                        System.out.println("unsuported sell type");  
		                    break;  
		                    }
	                	}
	                	if(cell.getColumnIndex()==3){
		                    System.out.println("Cell " + cell.getColumnIndex());  
			                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据  
			                    case HSSFCell.CELL_TYPE_NUMERIC:
			                    	toAddY=cell.getNumericCellValue();
			                    	System.out.println("num");
			                        break;  
			                    case HSSFCell.CELL_TYPE_STRING:  
			                        System.out.println(cell.getStringCellValue()); 
			                        toAddY = Float.parseFloat(cell.getStringCellValue());
			                        break;  
			                    case HSSFCell.CELL_TYPE_BOOLEAN:  
			                        System.out.println(cell.getBooleanCellValue());  
			                        break;  
			                    case HSSFCell.CELL_TYPE_FORMULA:  
			                        System.out.println(cell.getCellFormula());  
			                        break;  
			                    default:  
			                        System.out.println("unsuported sell type");  
			                    break;  
			                    }
		                }
	                	
		            }//cellend  
	                //toadd to = new toadd(toAddC,new GeoPoint(toAddX, toAddY));
	                //System.out.println("in toadd:"+to.ID);
	                System.out.println(toAddC);
	                points[row.getRowNum()-1]=new toadd(toAddC,new GeoPoint());
	                points[row.getRowNum()-1].Point.setX(toAddX);
	                points[row.getRowNum()-1].Point.setY(toAddY);
	            lastrow=row.getRowNum();
	            }//rowend   
	        } catch (IOException ex) {  
	            ex.printStackTrace();  
	        }  
	    }  
}
class toadd{
	String ID;
	GeoPoint Point;
	toadd(String id,GeoPoint point){
		this.ID = id;
		this.Point =point;
	}
	
}
