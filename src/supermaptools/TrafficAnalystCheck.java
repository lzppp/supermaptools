package supermaptools;
//检查公交换乘分析的数据是否准确

import java.util.HashMap;
import java.util.Iterator;

import com.supermap.data.*;
import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystCheckResult;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.analyst.networkanalyst.WeightFieldInfo;
import com.supermap.analyst.networkanalyst.WeightFieldInfos;
import com.supermap.analyst.trafficanalyst.LineSetting;
import com.supermap.analyst.trafficanalyst.RelationSetting;
import com.supermap.analyst.trafficanalyst.StopSetting;
import com.supermap.analyst.trafficanalyst.TransferAnalyst;
import com.supermap.analyst.trafficanalyst.TransferAnalystSetting;

public class TrafficAnalystCheck {
	public static String dataSourcePath ="./my.udb";
	
	/**
	 * @param args
	 */
	public  Datasource  openWorkspace(){
		// 打开工作空间，得到数据源
		Workspace workspace = new Workspace();
		Datasources datasources = workspace.getDatasources();
		DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo(
				dataSourcePath, "dataClassification", "");
		Datasource datasource = datasources.open(datasourceConnectionInfo);
		if (datasource == null) {
            System.out.println("打开数据源失败");
            
    } else {
            System.out.println("数据源打开成功！");
            }

		return datasource;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrafficAnalystCheck ta = new TrafficAnalystCheck();
		Datasource datasource = ta.openWorkspace();	
		//公交线数据
		DatasetVector bl = (DatasetVector) datasource.getDatasets().get("BusLine");
		//公交站点
		DatasetVector bs = (DatasetVector) datasource.getDatasets().get("BusPoint");
		//关系表
		DatasetVector relation = (DatasetVector) datasource.getDatasets().get("LineStopRelation");

		// 构造交通换乘分析环境 其他设置保持默认值
		TransferAnalyst  analyst = new TransferAnalyst();
//		TransportationAnalystSetting tASetting = analyst.getAnalystSetting();
//
//		WeightFieldInfos weightInfos = new WeightFieldInfos();
//		WeightFieldInfo weightInfo = new WeightFieldInfo();
//		weightInfo.setName("SMLENGTH");
//		weightInfo.setFTWeightField("SMLENGTH");
//		weightInfo.setTFWeightField("SMLENGTH");
//		weightInfos.add(weightInfo);
//		tASetting.setWeightFieldInfos(weightInfos);
//		tASetting.setNetworkDataset(network);
//		tASetting.setEdgeIDField("SmEdgeID");
//		tASetting.setNodeIDField("SmNodeID");
//		tASetting.setFNodeIDField("SMFNODE");
//		tASetting.setTNodeIDField("SMTNODE");

		// 数据检查
		TransferAnalystSetting tas = new TransferAnalystSetting();
		//设置线路
		LineSetting ls = new LineSetting();
		ls.setAliasField("BusLine");
		ls.setDataset(bl);
		ls.setLengthField("SmLength");
		ls.setLineIDField("LineID");
		ls.setNameField("Name");
		tas.setLineSetting(ls);
		
		//设置关系
		RelationSetting rs = new RelationSetting();
		rs.setDataset(relation);
		rs.setLineIDField("LineID");
		rs.setStopIDField("StopID");
		rs.setSerialNumField("StopIndex");
		tas.setRelationSetting(rs);
		
		//设置站点
		StopSetting ss = new StopSetting();
		ss.setAliasField("BusPoint");
		ss.setDataset(bs);
		ss.setNameField("Name");
		ss.setStopIDField("StopID");
		tas.setStopSetting(ss);
		
		
		tas.setSnapTolerance(50);
		tas.setUnit(Unit.METER);
		tas.setWalkingTolerance(100);
		
		Boolean CheckResult = analyst.check(tas);
		
		
//		HashMap<Integer, Integer> arrEdgeInfo = new HashMap<Integer, Integer>();
//		arrEdgeInfo = CheckResult.getArcErrorInfos();
//		
//		HashMap<Integer, Integer> arrNodeInfo = new HashMap<Integer, Integer>();
//		arrNodeInfo = CheckResult.getNodeErrorInfos();
//		
//		HashMap<Integer, Integer> turnErrorInfo = new HashMap<Integer, Integer>();
//		turnErrorInfo = CheckResult.getTurnErrorInfos();
//		
//		System.out.println(arrEdgeInfo.size());
//		Iterator<Integer> ite = arrEdgeInfo.keySet().iterator();
//		int key = 0;
//		while (ite.hasNext()) {
//			key = ite.next();
//			System.out.println(key + ":" + arrEdgeInfo.get(key));
//		}
//		System.out.println(arrNodeInfo.size());
//		System.out.println(turnErrorInfo.size());
		if(CheckResult)
		{
			System.out.println("数据正确，可以加载");
//			TestAssert.assertTrue("load", analyst.load());
			
		}
		else
		{
				System.out.println("数据有错误，请检查数据");
			
		}
	}

}
