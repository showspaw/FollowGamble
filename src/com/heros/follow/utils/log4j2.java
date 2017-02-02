package com.heros.follow.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class log4j2 {

	private static log4j2 instance;
	private LoggerContext context =(LoggerContext)LogManager.getContext(false);
	private Logger logger = null;
	private String lastUseLogger = "";
	
	private String filePath = "logs";

	private log4j2() {
	    File file = new File("src/main/resources/log4j2.xml");
		context.setConfigLocation(file.toURI());
//		context.getLogger("org.apache.http").setLevel(Level.OFF);
	}
	
	public void removeLog(int beforeDay) {
		File file = new File(filePath);
		String nowdata = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - (beforeDay * 24 * 60 * 60 * 1000)));
	    if(file.isDirectory()){
	        for (String fileName : file.list()) {
	        	if (fileName.indexOf(nowdata) != -1)  {
	        		if (new File(filePath + "\\" + fileName).delete()) {
	        			System.out.println(fileName + " 刪除成功");
	        		} else {
	        			System.out.println(fileName + " 刪除失敗");
	        		}
	        	}
	        }
	    }
	}
	
	public String getfileName2(String s) {
		int dotPos = s.lastIndexOf("resources");
		return s.substring(0, dotPos);
	}

	public void setLog(String loguser, String msg) {
		if (logger == null || !lastUseLogger.equals(logger)) {
			logger = LogManager.getLogger(loguser);
			lastUseLogger = loguser;
//			context.reconfigure();
		}
		 ThreadContext.push(UUID.randomUUID().toString());
		 logger.fatal(msg);
	 	ThreadContext.pop();
	 }
	
	public static log4j2 getInstance() {
		if (instance == null) {
			synchronized (log4j2.class) {
				if (instance == null) {
					instance = new log4j2();
				}
			}
		}
		return instance;
	}
}
