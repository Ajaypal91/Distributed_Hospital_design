package com.dc.util;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;


public class CurrentProperties {
	
	public final static Logger logger = Logger.getLogger(CurrentProperties.class);
	public static final String propFile = "myconf.properties";
	
	public static String getCurrentPolicy() {
		
		logger.debug("Getting Current Policy.");
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propFile);	
		}
		catch (Exception e) {
			String msg = "Failed to retrieve the policy";
			logger.error(msg);
			return null;
		}
		
		if (config.getProperty("policy") != null) 
			return config.getProperty("policy").toString();
		else
			return null;
		
	}
	
	public static Boolean setCurrentPolicy(String policy) {
		logger.debug("Setting Current Policy");
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration(propFile);
			config.setProperty("policy", policy);
			config.save();
			
		}
		catch (Exception e) {
			String msg = "Failed to update the policy = ";
			logger.error(msg + policy);
			return false;
		}
		logger.debug("Current Policy updated to "+ policy);
		
		return true;
	}
	
	
	public static Boolean getCurrentCacheStatus() {
		
		logger.debug("Getting Current Cache Status.");
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propFile);	
		}
		catch (Exception e) {
			String msg = "Failed to retrieve the current Cache Status";
			logger.error(msg);
			return null;
		}
		
		if (config.getProperty("isCacheUpdating") != null) 
			return Boolean.getBoolean(config.getProperty("isCacheUpdating").toString());
		else
			return null;
		
	}
	
	public static Boolean setCurrentCacheStatus(Boolean status) {
		logger.debug("Setting Current Cache Status");
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration(propFile);
			config.setProperty("isCacheUpdating", status);
			config.save();
			
		}
		catch (Exception e) {
			String msg = "Failed to update the Current Cache Status = ";
			logger.error(msg + status);
			return false;
		}
		logger.debug("Current Cache Status updated to "+ status);
		
		return true;
	}
	
   public static Boolean isCronInitiated() {
		
		logger.debug("Getting Current Cron Status.");
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propFile);	
		}
		catch (Exception e) {
			String msg = "Failed to retrieve the Cron Status";
			logger.error(msg);
			return null;
		}
		
		if (config.getProperty("isCronInitiated") != null) 
			return Boolean.getBoolean(config.getProperty("isCronInitiated").toString());
		else
			return null;
		
	}
	
	public static Boolean setCronInitiatedStatus(Boolean status) {
		logger.debug("Setting Current Cache Status");
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration(propFile);
			config.setProperty("isCronInitiated", status);
			config.save();
			
		}
		catch (Exception e) {
			String msg = "Failed to update the Cron Status = ";
			logger.error(msg + status);
			return false;
		}
		logger.debug("Cron Status updated to "+ status);
		
		return true;
	}

	public static Integer getLastPatientId() {
		
		logger.debug("Getting Last Patient ID.");
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propFile);	
		}
		catch (Exception e) {
			String msg = "Failed to retrieve the Last Patient ID";
			logger.error(msg);
			return null;
		}
		
		return Integer.parseInt(config.getProperty("id").toString());
	}
	
	public static Boolean setLastPatientId(Integer id) {
		logger.debug("Setting Last Patient ID");
		try {
			
			PropertiesConfiguration config = new PropertiesConfiguration(propFile);
			config.setProperty("id", id);
			config.save();
			
		}
		catch (Exception e) {
			String msg = "Failed to update the Last Patient ID = ";
			logger.error(msg + id);
			return false;
		}
		logger.debug("Cron Status updated to "+ id);
		
		return true;
	}
	
}
