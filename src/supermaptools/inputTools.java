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
		//��ȡexcel
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
	        // ���幤���ռ�
	        Workspace workspace = new Workspace();
	        Datasources datasources = workspace.getDatasources();
	        // ��������Դ������Ϣ������������������Դ���ö�����
	        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo(
					"./my.udb", "test", "");
	        
			//datasourceConnectionInfo.setEngineType(EngineType.UDB);
			if (datasourceConnectionInfo != null){
				System.out.println(datasourceConnectionInfo.toString());
			}
			
			Datasource datasource = datasources.open(datasourceConnectionInfo);
			if (datasource == null) {
                System.out.println("������Դʧ��");
                return;
			} else {
                System.out.println("����Դ�򿪳ɹ���");
			}
			Datasets datasets = datasource.getDatasets();
			Dataset dataset = datasets.get("New_Point");
			if (dataset==null)System.out.println("������Դʧ��");
			DatasetVector datasetVector = (DatasetVector) dataset;

			//�õ����ݼ��ļ�¼��
			Recordset recordset = datasetVector.getRecordset(false,
					CursorType.DYNAMIC);
			//Recordset recordset = datasetVector.getRecordset(true,CursorType.DYNAMIC);
			if (recordset == null) {
                System.out.println("��recordʧ��");
                return;
			} else {
                System.out.println("record�򿪳ɹ���");
			}

	        // ������������ÿ���ύ�ļ�¼��Ŀ
	        //editor.setMaxRecordCount(lastrow-1);
	        // �� World ���ݼ��ж�ȡ���ζ�����ֶ�ֵ���������µ� example ���ݼ���
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
			
			
	        // ��������ͳһ�ύ
	        editor.update();

	        // �ͷż�¼��
	        recordset.dispose();
	        
	        
	        // �ͷŹ����ռ���Դ
	        datasourceConnectionInfo.dispose();
	        workspace.dispose();

	    }
		public static void readXml(String fileName){  
	        boolean isE2007 = false;    //�ж��Ƿ���excel2007��ʽ  
	        if(fileName.endsWith("xlsx"))  
	            isE2007 = true;  
	        try {  
	            InputStream input = new FileInputStream(fileName);  //����������  
	            Workbook wb  = null;  
	            //�����ļ���ʽ(2003����2007)����ʼ��  
	            if(isE2007)  
	                wb = new XSSFWorkbook(input);  
	            else  
	                wb = new HSSFWorkbook(input);  
	            Sheet sheet = wb.getSheetAt(0);     //��õ�һ����  
	            Iterator<Row> rows = sheet.rowIterator(); //��õ�һ�����ĵ�����  
	            while (rows.hasNext()) {
	                Row row = rows.next();  //��������� 
	                if (row.getRowNum()==0)
	            		continue;
	                System.out.println(row.getRowNum());
	                Iterator<Cell> cells = row.cellIterator();    //��õ�һ�еĵ�����
	                String toAddC = null;
	                double toAddX = 0;
	                double toAddY = 0;
	                while (cells.hasNext()) { 
	                	Cell cell = cells.next();
	                	if(cell.getColumnIndex()==4){
		                    //System.out.println("Cell #" + cell.getColumnIndex());  
			                    switch (cell.getCellType()) {   //����cell�е��������������  
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
		                    switch (cell.getCellType()) {   //����cell�е��������������  
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
			                    switch (cell.getCellType()) {   //����cell�е��������������  
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
