package com.java.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.java.service.IPolicyService;


@RestController
@RequestMapping("/policy")
public class PolictyController {
	
	public final static Logger logger = Logger.getLogger(PolictyController.class);
	@Autowired
	IPolicyService policyService;

	/**
	 * API to get get the policy of the server
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String getPolicy() {
		
		logger.debug("Policy get request received");
		return policyService.getPolicy();
		
	}
	
	/*
	 * API to set the policy at the policy server
	 */
	@RequestMapping(value = "/set", method = RequestMethod.PUT)
	public Boolean setPolicy(@RequestBody String policy) {
		logger.debug("Request to set policy " + policy + "received");
		return policyService.setPolicy(policy);
	}
	

}
