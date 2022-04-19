package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.DataMaster;
import setting.LogController;

public class DataMasterDAO {
	
	public static final String CUSTOMER_APPLICATION_QUERY = 
			"SELECT [id],[create_date_time],[update_date_time],[ref_no],[form_id],[ip_address],[browser_agent],[channel_code],[submit_lang],[channel],[status] FROM [P03_customer_application] where [ref_no] = ? AND [form_id] = ?; ";
	
	public static int creatMasterInDB(DataMaster dm, Connection conn) {
		int master_id=-1;
		String master_id_str="";
		PreparedStatement masterPreStmt = null;
		
		//Save master			
		String masterSql="INSERT INTO [eForm].[dbo].[P03_customer_application]"
				+ "(create_date_time,"
				+ "update_date_time,"
				+ "ref_no,"
				+ "form_id,"
				+ "ip_address,"
				+ "browser_agent,"
				+ "channel_code,"
				+ "submit_lang,"
				+ "channel,"					
				+ "status) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?);";					
		
		try {
			int i=1;
			masterPreStmt = conn.prepareStatement(masterSql, PreparedStatement.RETURN_GENERATED_KEYS);			
			masterPreStmt.setTimestamp(i++, dm.getCreate_date_time());
			masterPreStmt.setTimestamp(i++, dm.getUpdate_date_time());
			masterPreStmt.setString(i++, dm.getRef_no());		
			masterPreStmt.setString(i++, dm.getForm_id());
			masterPreStmt.setString(i++, dm.getIp_address());
			masterPreStmt.setString(i++, dm.getBrowser_agent());
			masterPreStmt.setString(i++, dm.getChannel_code());
			masterPreStmt.setString(i++, dm.getSubmit_lang());
			masterPreStmt.setString(i++, dm.getChannel());
			masterPreStmt.setString(i++, dm.getStatus());										
			
			// execute insert SQL statement
			masterPreStmt.executeUpdate();
			LogController.writeMessage(LogController.INFO, "DataMasterDAO", "creatMasterInDB", "Complete writing master");
			ResultSet generatedKeys = masterPreStmt.getGeneratedKeys();
			
			while (generatedKeys.next()) {
				master_id_str = String.format("%014d", generatedKeys.getInt(1));
				master_id = Integer.parseInt(master_id_str);
				LogController.writeMessage(LogController.INFO, "DataMasterDAO", "creatMasterInDB", "Retrieved auto-gen master_id from db : " + master_id);
			}
			
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "creatMasterInDB", e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				if(masterPreStmt != null)
					masterPreStmt.close();
			} catch (SQLException e) {				
				LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "creatMasterInDB", e.getMessage());
				e.printStackTrace();
			}
		}								
	
		
		return master_id;
	}
	
	public static int updateMasterinDB(DataMaster dm, Connection conn){
		int result=-1;
		PreparedStatement masterPreStmt = null;
		
		//update master
		String updateMasterSQL = "UPDATE [eForm].[dbo].[P03_customer_application] "
				+ "SET [update_date_time]=?, "
				+ "[status]=? "					
				+ "WHERE [id]=?;";
		
		if ("s02".equals(dm.getForm_id())) {
			updateMasterSQL = "UPDATE [eForm].[dbo].[P03_customer_application] "
					+ "SET [update_date_time]=?, "
					+ "[ip_address]=?, "
					+ "[browser_agent]=?, "
					+ "[submit_lang]=?, "
					+ "[channel]=?, "
					+ "[status]=? "	
					+ "WHERE [id]=?;";
		}
		
		try{				
			masterPreStmt = conn.prepareStatement(updateMasterSQL);
			int i=1;
			masterPreStmt.setTimestamp(i++, dm.getUpdate_date_time());
			
			if ("s02".equals(dm.getForm_id())) {
				masterPreStmt.setString(i++, dm.getIp_address());
				masterPreStmt.setString(i++, dm.getBrowser_agent());
				masterPreStmt.setString(i++, dm.getSubmit_lang());
				masterPreStmt.setString(i++, dm.getChannel());
			}
			
			masterPreStmt.setString(i++, dm.getStatus());
			masterPreStmt.setInt(i++, dm.getMaster_id());
			
			// execute insert SQL statement
			masterPreStmt.executeUpdate();
			result=0;
		} catch(SQLException sqle){
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "updateMasterinDB", sqle.getMessage());
		} catch(NumberFormatException nfe){
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "updateMasterinDB", nfe.getMessage());
		} finally{
		}
		
		return result;
	}
	
	public static int getMasterIdinDB(String refNo, String formId, Connection conn){
		int result = -1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {	
			stmt = conn.prepareStatement(CUSTOMER_APPLICATION_QUERY);
			
			int i=1;
			stmt.setString(i++, refNo);
			stmt.setString(i++, formId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("id");
			}
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getMasterIdinDB", e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getMasterIdinDB", e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}
	

	public static String getStatusinDB(String refNo, String formId, Connection conn){
		String result = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {	
			stmt = conn.prepareStatement(CUSTOMER_APPLICATION_QUERY);
			
			int i=1;
			stmt.setString(i++, refNo);
			stmt.setString(i++, formId);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("status");
			}
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getStatusinDB", e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getStatusinDB", e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getUpdateDateTimeinDB(String refNo, String formId, Connection conn){
		String result = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {	
			stmt = conn.prepareStatement(CUSTOMER_APPLICATION_QUERY);
			
			int i=1;
			stmt.setString(i++, refNo);
			stmt.setString(i++, formId);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getTimestamp("update_date_time").toString();
			}
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getUpdateDateTimeinDB", e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "getUpdateDateTimeinDB", e.getMessage());
				e.printStackTrace();
			}
		}
		LogController.writeMessage(LogController.DEBUG, "DataMasterDAO", "getUpdateDateTimeinDB result: ",result);
		return result;
	}
}
