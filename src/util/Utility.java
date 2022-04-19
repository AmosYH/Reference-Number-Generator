package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import setting.LogController;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;



public class Utility {
	private static Properties config = new Properties();
	
	static {
		try {
			File configFile = new File("resource/config/config.properties");
			config.load(new FileInputStream(configFile));
		} catch (Exception e) {
			LogController.writeMessage(LogController.ERROR, "DBManager", "makeConnection", "init file error: "+e);
			LogController.writeExceptionMessage(LogController.DEBUG, e);
		}
	}
	
	public static String getProperty(String key, String defVal){
		String propVal = getProperty(key);
		if("".equals(propVal)){
			return defVal;
		}else{
			return propVal;
		}	
	}
	public static String getProperty(String key) {
		if (key == null)
			return "";

		String v = config.getProperty(key);
		return v.trim();
	}
	public static String nullFilter(String input){
		if(input == null)
			return "";
		return input;
	}
	
	
	
	public static File EncryptInZipFile(String zipFileName, ArrayList<File> filesList, String password) throws ZipException{
		File newFile = null;
		
		try{
			newFile = new File(zipFileName + ".zip");
			ZipFile newZipFile = new ZipFile(newFile);
	  	    ZipParameters parameters = new ZipParameters();
	  	    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
	  	    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
	  	    
	  	    if (password != null && !"".equals(password)){
		  	    parameters.setEncryptFiles(true);
		  	    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
		  	    parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
		  	    parameters.setPassword(password);
	  	    }else
	  	    	parameters.setEncryptFiles(false);
	  	    
	  	    newZipFile.addFiles(filesList, parameters);  	 	    
	  	    
	  	    return newFile;
		}catch (ZipException e){
			e.printStackTrace();
			throw e;
		}
		
  	    
	}
	
	public static String padInput(String input, int padLength, String padValue, String padDirection) {
		if ("right".equals(padDirection))
			return StringUtils.rightPad(input, 20, "0");
		else
			return StringUtils.leftPad(input, 20, "0");
	}
	
	
}


