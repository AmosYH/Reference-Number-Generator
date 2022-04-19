package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import setting.LogController;
import util.Base64Coder;
import util.CommonUtil;
import util.DBManager;
import util.Utility;

public class main {
	
	static {
		LogController.createLogFile();
	}
	
	public static void main(String[] args){
		
		LogController.writeMessage(LogController.DEBUG, "Start EDM RefNumGen program");
		LogController.writeMessage(LogController.DEBUG, "Start time: " + new Timestamp(new Date().getTime()));

		Connection conn = null;
		int count = 0;
		String refNum = "";
		String codedRefNum = "";
		String url = "";
		
		StringBuilder sb = new StringBuilder();
		String[] colHeaderList = new String[] {"refNum", "url"};
		String colHeaderString = "";
    	for(String headerItem : colHeaderList){
			colHeaderString = String.valueOf(colHeaderString) + headerItem + ",";
		}
    	colHeaderString = String.valueOf(colHeaderString) + "\n";
    	sb.append(colHeaderString);
		
		try{
			conn = DBManager.makeConnection();
			conn.setAutoCommit(false);
			count=Integer.parseInt(Utility.getProperty("count"));
			
			for (int i=0;i<count;i++) {
				String colValue = "";
				String seqNum = CommonUtil.getSeqNum(conn);
//				String seqNum = "0000000001";
				String yyMMdd = Utility.getProperty("date");
				refNum = "EM"+yyMMdd+seqNum;
				codedRefNum=Base64Coder.encodeString(refNum);
				url = "https://whp.hkbea.com/hk/edm/form?type=s03&language=en&refNo="+codedRefNum;
				
				LogController.writeMessage(LogController.DEBUG, "refNum: "+refNum);
				LogController.writeMessage(LogController.DEBUG, "codedRefNum: "+codedRefNum);
				LogController.writeMessage(LogController.DEBUG, "url: "+url);
				
				DBManager.storeToDB(conn, refNum, url);
				colValue += refNum+",";
				colValue += url+",";
				
				sb.append(String.valueOf(colValue)+"\n");
			}
			createCSVFile(sb, "record");
			
		} catch(Exception e){
			LogController.writeMessage(LogController.ERROR, e.getMessage());
			LogController.writeExceptionMessage(LogController.ERROR, e);
		} finally{
			if (conn!=null){
				try {
					conn.commit();
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					LogController.writeMessage(LogController.ERROR, e.getMessage());
					LogController.writeExceptionMessage(LogController.DEBUG, e);
				}
				DBManager.closeConnection(conn);
			}
		}
		
		LogController.writeMessage(LogController.DEBUG, "Finish EDM RefNumGen program");
		LogController.writeMessage(LogController.DEBUG, "End time: " + new Timestamp(new Date().getTime()));
	}
	
	private static void createCSVFile(final StringBuilder stringBuilder, final String fileName) throws FileNotFoundException {
        final Date date = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        final String path_bak = String.valueOf(Utility.getProperty("backup_path")) + fileName.replace(".", "_") + "_" + simpleDateFormat.format(date) + ".csv";
        LogController.writeMessage("DEBUG", "Create File Start");
        final PrintWriter pw = new PrintWriter(new File(path_bak));
        pw.write(stringBuilder.toString());
        pw.close();
        LogController.writeMessage("DEBUG", "Create File Success");
        LogController.writeMessage("DEBUG", "Path : " + path_bak);
    }
    
	
}