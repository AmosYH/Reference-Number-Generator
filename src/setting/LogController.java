package setting;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import util.CommonUtil;

public class LogController {
	public static final String DEBUG = "DEBUG";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	private static Properties logProperties = new Properties();
	private static String logLevel = "";
	
	
	
	private static void initLogLevel(){
		try{
			File logFile = new File("resource/config/log.properties");
			logProperties.load(new FileInputStream(logFile));
			logLevel = CommonUtil.getProperty(logProperties, "log_level");
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param logType
	 * @return true if write log into file
	 */
	private static boolean logLevelChecking(String logType){
		if(INFO.equals(logLevel) && DEBUG.equals(logType))
			return false;
		if( (ERROR.equals(logLevel) && DEBUG.equals(logType)) || (ERROR.equals(logLevel) && INFO.equals(logType)) )
			return false;
		return true;
	}

	/**
	 * @param logType
	 * @param msg
	 * @throws Exception
	 * write message in SystemOut.log file
	 */
	private static synchronized void write(String logType, String[] msg) throws Exception{
		String message = logType + "\t" ;
		for(int i = 0; i < msg.length; i ++) {
			message += msg[i] + "\r\n";
		}
		
		System.out.print(message);
	}
	
	private static void writeException(String logType, Exception e) {
		try {
			initLogLevel();
			StackTraceElement[] trace = e.getStackTrace();
			String[] msgs = new String[trace.length + 1];
			msgs[0] = e.toString();
			
	        for (int i = 0; i < trace.length; i++) {
	        	msgs[i + 1] = ("\tat " + trace[i]);
	        }
	        
	        write(logType, msgs);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	
	private static String getCallerFileName() {
		return Thread.currentThread().getStackTrace()[3].getFileName();
	}
	private static String getCallerMethodName() {
		return Thread.currentThread().getStackTrace()[3].getMethodName();
	}
	
	/**
	 * Simple Debug Log
	 * @param logType (DEBUG, INFO, ERROR)
	 * @param fileName (jspFileName, javaClassName)
	 * @param msg
	 */
	public static void writeMessage(String msg) {
		writeMessage(DEBUG, msg);
	}
	
	/**
	 * 
	 * @param logType (DEBUG, INFO, ERROR)
	 * @param fileName (jspFileName, javaClassName)
	 * @param msg
	 */
	public static void writeMessage(String logType, String msg) {
		writeMessage(logType, getCallerFileName(), getCallerMethodName(), msg,"");
	}
	/**
	 * 
	 * @param logType (DEBUG, INFO, ERROR)
	 * @param msg
	 * @param sessionId
	 */
	public static void writeMessage(String logType, String fileName, String msg, String sessionId) {
		writeMessage(logType, getCallerFileName(), getCallerMethodName(), msg,sessionId);
		
	}
	/**
	 * 
	 * @param logType (DEBUG, INFO, ERROR)
	 * @param fileName (jspFileName, javaClassName)
	 * @param methodName
	 * @param msg
	 */
	private static void writeMessage(String logType, String fileName, String methodName, String msg, String sessionId){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.sss");
		String outMsg = dateFormat.format(new Date()) + "\t";
		
		if (!sessionId.equals(""))
			outMsg += sessionId + "\t";
			outMsg +=  fileName + "\t" + methodName + "\t" + msg;
		
		try {
			if(logLevelChecking(logType)){
				write(logType, new String[] {outMsg});
			}
		} catch (Exception e) {
			LogController.writeExceptionMessage(LogController.ERROR, e);
		}
	}

	/**
	 * 
	 * @param logType (DEBUG, INFO, ERROR)
	 * @param e 
	 */
	public static void writeExceptionMessage(String logType, Exception e){
		if(logLevelChecking(logType)){
			writeException(logType, e);
		}
	}

	public static void createLogFile() {
		System.out.println("Start CreateLogFile");
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_sss");
			PrintStream fileOut = new PrintStream("log/logFile_"+dateFormat.format(new Date())+".txt");
			System.setOut(fileOut);
			System.setErr(fileOut);

			System.out.println("CreateLogFile Successful");
		} catch (IOException e) {
			System.out.println("CreateLogFile FAIL");
		}
	}
}
