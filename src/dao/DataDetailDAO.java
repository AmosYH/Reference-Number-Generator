package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.Attribute;
import model.DataDetail;
import setting.LogController;

public class DataDetailDAO {
	
	public static final String CUSTOMER_APPLICATION_DETAIL_QUERY = 
			"SELECT [master_id],[field_name],[field_value],[step_id],[section_id],[encrypted] FROM [P04_customer_application_detail] where [master_id] = ? AND [field_name] = ?; ";
	
	private static void createEncryptedDetail(String key, DataDetail detail, Connection conn) throws Exception{
		PreparedStatement preStmt = null;
		
		String sql = "exec [dbo].[sp_OpenEncryptionKeys];"
				+ "INSERT INTO [eForm].[dbo].[P04_customer_application_detail]"
				+ "(master_id,field_name,field_value,step_id,section_id,encrypted) "
				+ "VALUES (?,?,[dbo].[Encrypt](?),?,?,'Y');";
		
			preStmt = conn.prepareStatement(sql);
			int j=1;
			preStmt.setInt(j++, detail.getMaster_id());
			preStmt.setString(j++, detail.getField_name());
			preStmt.setNString(j++, detail.getField_value());
			preStmt.setInt(j++, detail.getStep_id());
			preStmt.setInt(j++, detail.getSection_id());
			preStmt.executeUpdate();
		
		
	}
	private static void updateEncryptedDetail(String key, DataDetail detail, Connection conn) throws Exception{
		PreparedStatement preStmt = null;
		
		String sql = "exec [dbo].[sp_OpenEncryptionKeys];"
				+ "UPDATE [eForm].[dbo].[P04_customer_application_detail] "
				+ "SET [field_value]=[dbo].[Encrypt](?) "
				+ "WHERE [master_id]=? and  [field_name]=?;";
		
			preStmt = conn.prepareStatement(sql);
			int i=1;
			preStmt.setNString(i++, detail.getField_value());
			preStmt.setInt(i++, detail.getMaster_id());
			preStmt.setString(i++, detail.getField_name());
			preStmt.executeUpdate();
		
	}
	public static int CreateDetailinDB(HashMap<String, DataDetail> tmp_details, Connection conn){
		int result=-1;
		PreparedStatement detailPreStmt_insert = null;
		
		String detailSql="INSERT INTO [eForm].[dbo].[P04_customer_application_detail]"
				+ "(master_id,field_name,field_value,step_id,section_id) "
				+ "VALUES (?,?,?,?,?);";
				
		try {	
			detailPreStmt_insert = conn.prepareStatement(detailSql);
			for (DataDetail detail:tmp_details.values()){
				if(detail.getField_name().contains("creditCardNum")){
					//run encryption SQL for credit card number
					//LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "UpdateDetailinDB", "insert password" + detail.getField_value() );
					createEncryptedDetail(detail.getField_name(), detail, conn);
				} else{
					//batch flow without encryption
					int j=1;								
					//LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "CreateDetailinDB", "Start Insert. Master id: " + detail.getMaster_id() +". Field Name: " + detail.getField_name());
					//Perform insert							
					detailPreStmt_insert.setInt(j++, detail.getMaster_id());
					detailPreStmt_insert.setString(j++, detail.getField_name());
					detailPreStmt_insert.setNString(j++, detail.getField_value());
					detailPreStmt_insert.setInt(j++, detail.getStep_id());
					detailPreStmt_insert.setInt(j++, detail.getSection_id());						
					detailPreStmt_insert.addBatch();
				}
			}												
			
			//execute batch			
			detailPreStmt_insert.executeBatch();
			LogController.writeMessage(LogController.INFO, "DataDetailDAO", "CreateDetailinDB", "Complete insert detail");
			result=0;
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "CreateDetailinDB", "sql" + e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException npe){
			LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "CreateDetailinDB", "npe" + npe.getMessage());
			npe.printStackTrace();
		} catch (Exception ee){
			LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "CreateDetailinDB", "ee" + ee.getMessage());
		}finally{
			try {
				if(detailPreStmt_insert != null)
					detailPreStmt_insert.close();
			} catch (SQLException e) {				
				LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "CreateDetailinDB", "finally" + e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}

	public static int UpdateDetailinDB(HashMap<String, DataDetail> tmp_details, Connection conn) {
		int result=-1;
		
		PreparedStatement detailPreStmt_update = null;
		String updateSQL = "UPDATE [eForm].[dbo].[P04_customer_application_detail] "
				+ "SET [field_value]=? "
				+ "WHERE [master_id]=? and  [field_name]=?;";
		
		try{				
			detailPreStmt_update = conn.prepareStatement(updateSQL);
			for (DataDetail detail:tmp_details.values()){
				if(detail.getField_name().contains("creditCardNum")){
					//run encryption SQL for credit card number
					//LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "UpdateDetailinDB", "update password" + detail.getField_value() );
					updateEncryptedDetail(detail.getField_name(), detail, conn);
				} else{
					int i=1;
					//LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "UpdateDetailinDB", "Start Update. master id: " + detail.getMaster_id() +". Field Name: " + detail.getField_name());
					detailPreStmt_update.setNString(i++, detail.getField_value());
					detailPreStmt_update.setInt(i++, detail.getMaster_id());
					detailPreStmt_update.setString(i++, detail.getField_name());
					detailPreStmt_update.addBatch();	
				}
			}
			//execute batch			
			detailPreStmt_update.executeBatch();
			LogController.writeMessage(LogController.INFO, "DataDetailDAO", "UpdateDetailinDB", "Complete update detail");
			result=0;
		} catch(SQLException sqle){
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "UpdateDetailinDB", sqle.getMessage());
		} catch(NumberFormatException nfe){
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "UpdateDetailinDB", nfe.getMessage());
		} catch(Exception ee){
			LogController.writeMessage(LogController.ERROR, "DataMasterDAO", "UpdateDetailinDB", ee.getMessage());
		} finally{
			//DBManager.closeConnection(conn);
		}
				
		
		return result;
	}	
	
	public static DataDetail queryDetail(int masterID, String fieldName, Connection conn){
		DataDetail dd = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		if(conn != null){
			try {
				stmt = conn.prepareStatement(CUSTOMER_APPLICATION_DETAIL_QUERY);
				stmt.setInt(1, masterID);
				stmt.setString(2, fieldName);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					dd = new DataDetail();
					dd.setMaster_id(rs.getInt("master_id"));
					dd.setField_name(rs.getString("field_name"));
					dd.setField_value(rs.getNString("field_value"));
					dd.setStep_id(rs.getInt("step_id"));
					dd.setSection_id(rs.getInt("section_id"));
					dd.setEncrypted(rs.getString("encrypted"));
				}			
			} catch (SQLException e) {
				LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "queryDetail - catch loop", e.getMessage());
			} finally {
				try {
					if(stmt != null)
						stmt.close();
				} catch (SQLException e) {
					LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "queryDetail - close statement", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return dd;
	}
	/**
	 * Insert if the detail is not exist in database
	 * Otherwise, update that record with input arg tmp_details
	 * @param tmp_details
	 * @param conn
	 * @return
	 */
	public static int overwriteDetail(HashMap<String, DataDetail> tmp_details, Connection conn){
		HashMap<String, DataDetail> insertMap = new HashMap<String, DataDetail>();
		HashMap<String, DataDetail> updateMap = new HashMap<String, DataDetail>();
		for(String key: tmp_details.keySet()){			
			DataDetail dd = tmp_details.get(key);
			DataDetail result = queryDetail(dd.getMaster_id(), dd.getField_name(), conn);
			if(result == null){
				insertMap.put(dd.getField_name(), dd);
			} else{
				updateMap.put(result.getField_name(), dd);
			}			
		}

		int updateResult = UpdateDetailinDB(updateMap, conn);
		//LogController.writeMessage(LogController.INFO, "DataDetailDAO", "overwriteDetail", "updateResult:"+updateResult);
		int insertResult = CreateDetailinDB(insertMap, conn);
		//LogController.writeMessage(LogController.INFO, "DataDetailDAO", "overwriteDetail", "insertResult:"+insertResult);
		if(updateResult == 0 && insertResult == 0){
			return 0;
		} else{
			return -1;
		}
	}
	
	public static boolean isDetailMatch(HashMap<String, String> recordToSearch, Connection conn){		
		PreparedStatement detailPreStmt = null;
		ResultSet rs = null;
		int rsCount = 0;
		
		String mainSQL = "SELECT [master_id],[field_name],[field_value],[step_id],[section_id],[encrypted]FROM [eForm].[dbo].[P04_customer_application_detail]"
				+ "WHERE [master_id]=? AND ([field_name] = ? AND [field_value] = ?)";
		String conditionSQL = "OR ([field_name] = ? AND field_value = ?)";
		
		for(int i = 0;i<recordToSearch.size()-2;i++){
			mainSQL += conditionSQL;
		}
		
		try{
			int i=1;
			detailPreStmt = conn.prepareStatement(mainSQL);
			for (String key : recordToSearch.keySet()) {
				if(key.equals("master_id")){
					detailPreStmt.setInt(i++, Integer.parseInt(recordToSearch.get(key)));
				} else{
					detailPreStmt.setString(i++, key);
					detailPreStmt.setString(i++, recordToSearch.get(key));
				}
	        }
			LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "isDetailMatch", "sql: " + detailPreStmt);
			
			rs = detailPreStmt.executeQuery();
			while (rs.next()) {
				LogController.writeMessage(LogController.DEBUG, "DataDetailDAO", "isDetailMatch", "rs: " + rs);
				rsCount++;
			}
			if(rsCount == recordToSearch.size()-1){
				return true;
			}
		} catch (SQLException e) {
			LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "isDetailMatch", e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				if(detailPreStmt != null)
					detailPreStmt.close();
			} catch (SQLException e) {
				LogController.writeMessage(LogController.ERROR, "DataDetailDAO", "isDetailMatch", e.getMessage());
				e.printStackTrace();
			}
		}
				
		return false;
	}
}
