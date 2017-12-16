package com.java.service;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PolicyServiceImpl implements IPolicyService {

	final static Logger logger = Logger.getLogger(PolicyServiceImpl.class);
	public final String propFile = "myconf.properties";
	
	@Override
	public String getPolicy() {
		
		logger.debug("Getting Policy.");
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(propFile);	
		}
		catch (Exception e) {
			String msg = "Failed to retrieve the policy";
			logger.error(msg);
			return null;
		}
		
		
		return config.getProperty("policy").toString();
	}

	@Override
	public Boolean setPolicy(String policy) {
		logger.debug("Setting Policy");
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
		logger.debug("Policy updated to "+ policy);
		
		return true;
	}
	
	

}
