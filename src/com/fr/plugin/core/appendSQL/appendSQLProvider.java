package com.fr.plugin.core.appendSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fr.data.impl.JDBCDatabaseConnection;
import com.fr.general.FRLogger;
import com.fr.stable.ParameterProvider;
import com.fr.stable.fun.impl.AbstractTableDataProvider;
import com.fr.stable.script.CalculatorProvider;
public class appendSQLProvider extends AbstractTableDataProvider {
	
	private static List<Map<String, String>> data;
	
	public appendSQLProvider() throws Exception {
		data = new ArrayList<Map<String, String>>();
		FRLogger.getLogger().info("init appendSQL");
	}
		
	@Override
	public String processTableDataSQL(ParameterProvider[] arg0, String arg1, CalculatorProvider arg2) {
		
//		//取得網址參數
//		arg2.eval("p1")

		
		try {
			getAppendSQL();
		} catch (Exception e) {
			FRLogger.getLogger().info(e.getMessage());
			e.printStackTrace();
		}
		
		if(data.size() > 0){
			Map<String,String> dataMap = data.get(0);
			FRLogger.getLogger().info("condition data : "+data.toString());
			arg1 += " and username = '"+ dataMap.get("login") +"'";
//			arg1 += " and username = '"+((Map)data.get(0)).get("login")+"'";
		}else{
			FRLogger.getLogger().info("data is empty");
		}
		
		for (ParameterProvider parameterProvider : arg0) {
			FRLogger.getLogger().info("------------------------------------------------------");
			FRLogger.getLogger().info("getName : " + parameterProvider.getName());
			FRLogger.getLogger().info("getValue : " + parameterProvider.getValue().toString());
			FRLogger.getLogger().info("------------------------------------------------------");
		}
		
		FRLogger.getLogger().info("print the finally SQL : "+arg1);
		return super.processTableDataSQL(arg0, arg1, arg2);
	}
	
	private void getAppendSQL() throws Exception{
		Map<String, String> dbConfig = loadDBXml.getDBConfig();
//		FRLogger.getLogger().info("dbConfig data : "+dbConfig.toString());
		Connection conn = new JDBCDatabaseConnection(
				dbConfig.get("driver"),
				dbConfig.get("url"),
				dbConfig.get("user"),
				dbConfig.get("password")).createConnection();
		
		String sql = "select login from bitnami_redmine.users where login = 'dogocreat'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Map<String, String>> data = convertData(rs);
		this.data = data;
		ps.close();
		rs.close();
	}
	
	private List<Map<String, String>> convertData(ResultSet rs) throws SQLException{
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int rsmdCount = rsmd.getColumnCount();
		Map<String, String> map = new HashMap<String, String>();
		while (rs.next()) {
			for (int i = 1; i <= rsmdCount; i++) {
				map.put(rsmd.getColumnName(i), rs.getString(i));
			}
			data.add(map);
		}
		return data;
	}
}
