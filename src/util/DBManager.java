package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dao.DataDetailDAO;
import dao.DataMasterDAO;
import model.DataMaster;
import model.DataDetail;
import setting.LogController;

public class DBManager {
	//private static final String jdbcName = "jdbc/eForm";
//	private final static String DB_DOMAIN = Utility.getProperty("host");
//	private final static String DB_DATABASENAME = Utility.getProperty("DB_DATABASENAME");
//	private final static String DB_USER = Utility.getProperty("db_user");
//	private final static String DB_PASSWORD = Utility.getProperty("db_password");
	private static Connection conn;
	

	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DBManager", "closeConnection", e.getMessage());
			LogController.writeExceptionMessage(LogController.DEBUG, e);
		}
	}
	
	
	 public static Connection makeConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String pw = new String(AES256.decryptByDefaultKey(Utility.getProperty("db_password")));
		    String url = "jdbc:sqlserver://" + Utility.getProperty("host") + 
		    			";user=" + Utility.getProperty("db_user") + 
		    			";password=" + pw;
			conn = DriverManager.getConnection(url);
			LogController.writeMessage(LogController.DEBUG, "DBManager", "makeConnection", "Success to make connection to database");
		} catch (Exception e) {
			LogController.writeMessage(LogController.ERROR, "DBManager", "makeConnection", e.getMessage());
			LogController.writeExceptionMessage(LogController.DEBUG, e);
		}
		return conn;
	}
	/*
	public static Connection makeConnection() {
		try {
			InitialContext context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(jdbcName);
			return dataSource.getConnection();
		} catch (Exception e) {
			logger.debug("makeConnection - e: " + e);
			return null;
		}
	}*/
	 
	 public static boolean storeToDB(Connection conn, String refNum, String url) {
			
			try {

				Timestamp ts = new Timestamp(new Date().getTime());
				
				//setup DataMaster
				DataMaster dm = new DataMaster(ts, ts, -1, refNum, "s03", "0.0.0.0", "", "", "en", "", "init", null);
				
				//setup DataDetail
				ArrayList<String> keys = new ArrayList<String>();
				keys.add("eDM_url");
				
				ArrayList<String> values = new ArrayList<String>();
				values.add(url);				

				
				LogController.writeMessage(LogController.DEBUG, "DBManager", "storeToDB", "Create DB Master!");
				
				// Create Master							
				int master_id = DataMasterDAO.creatMasterInDB(dm, conn);
				if (master_id != -1)
					dm.setMaster_id(master_id);
				else {
					LogController.writeMessage(LogController.ERROR, "DBManager", "storeToDB", "master_id is null or -1");
					return false;
				}
				LogController.writeMessage(LogController.DEBUG, "DBManager", "storeToDB", "Master is created. Master ID: " + master_id);
				
				// Create Detail
				LogController.writeMessage(LogController.DEBUG, "DBManager", "storeToDB", "Create Detail");
				HashMap<String, DataDetail> tmp_details = CommonUtil.createAllDetail( dm.getMaster_id(), keys.toArray(new String[keys.size()]), values.toArray(new String[values.size()]), 0, 0);
				LogController.writeMessage(LogController.INFO, "DBManager", "storeToDB", tmp_details.toString());
				
				String[] tmpKeys = tmp_details.keySet().toArray(new String[tmp_details.size()]);
				
				// insert detail to handler master
				for (String tmpKey : tmpKeys) {
					if (dm.getDetails() == null) {
						dm.setDetails(tmp_details);
					} else {
						dm.getDetails().put(tmpKey, tmp_details.get(tmpKey));
					}

				}
				// Call Dao Create Detail
				int result = DataDetailDAO.CreateDetailinDB(tmp_details, conn);
				if (result != -1){
					LogController.writeMessage(LogController.DEBUG, "DBManager", "storeToDB", "Detail Inserted.");
					return true;
				}		
				else {
					LogController.writeMessage(LogController.ERROR, "DBManager", "storeToDB", "Detail Insert failed.");
					return false;
				}

				
			} catch (Exception e) {
				LogController.writeMessage(LogController.ERROR, "DBManager", "storeToDB", "Exception: " + e);
				return false;
			} 
			
			
		}

}
