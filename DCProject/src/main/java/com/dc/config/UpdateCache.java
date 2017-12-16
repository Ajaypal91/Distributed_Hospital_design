package com.dc.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import com.dc.component.Patient;
import com.dc.service.PatientDaoService;
import com.dc.util.CurrentProperties;
import com.dc.util.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class UpdateCache implements Runnable{

	public static final String policyServerURL = "http://localhost:8080/PolicyServer/policy/get";
	public final static Logger logger = Logger.getLogger(UpdateCache.class);
	private PatientRepository patientRepository;
	
	//current policy status file
	public final String propFile = "myconf.properties";
	
	public UpdateCache(PatientRepository patientRepo) {
		this.patientRepository = patientRepo;	
	}
	
	//hazelcast cache updated every 1 min
	@Override
	public void run() {
		
		//set up HazelCast configuration
		Config cfg = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		IMap<Integer, Object> mapPatients = instance.getMap("patients");
		try {
			
			while(true) {
				
				logger.debug("Thread initialized for cache to update it");
				
				//retrieve the policy from policy server
				URL url = new URL(policyServerURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				if (conn.getResponseCode() != 200) {
					logger.error("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String newPolicy;
				newPolicy = br.readLine();
				logger.debug("policy found from the server: "+ newPolicy);
				
				//get current policy at this server
				String currentPolicy = CurrentProperties.getCurrentPolicy();
				logger.debug("current policy: "+ currentPolicy);
				
				//if the policy is updated on policy server
				if (currentPolicy == null || !newPolicy.equals(currentPolicy)) {
				
					if (newPolicy != null) {
						//update status of cache being updated
						CurrentProperties.setCurrentCacheStatus(true);
						//write logic to update the Hazelcast cache
						List<Patient> res = PatientDaoService.getPatientByStatus(newPolicy, patientRepository);
						logger.debug("records retrieved: " + res.size());
						//update the cache
						mapPatients.clear();
						for (Patient patient : res) {
							mapPatients.set(patient.getId(), (Patient)patient);
						}
						CurrentProperties.setCurrentCacheStatus(false);
						logger.debug("Number of records update in hazelcast cache : " + mapPatients.size());
						ObjectMapper mapper = new ObjectMapper();
						logger.debug("Records = "+mapper.writeValueAsString(res));
						//update current policy
						CurrentProperties.setCurrentPolicy(newPolicy);
					}
					
				}
				else {
					logger.debug("current policy same as server policy. Not updating the cache");
				}
				//close connection
				conn.disconnect();				
				
				//wait for 1 min
				Thread.sleep(60000);
				
			}
		}
		catch (Exception e) {
			
			logger.error("Cache thread experienced an error: " + e.getMessage());

		}
		
	}

}
