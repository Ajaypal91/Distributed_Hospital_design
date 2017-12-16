package com.dc.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dc.config.UpdateCache;
import com.dc.postprocessing.MyActiveMq;
import com.dc.service.PatientDaoService;
import com.dc.util.CurrentProperties;
import com.dc.util.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class PatientServiceImpl implements IPatientService{

	public final static Logger logger = Logger.getLogger(PatientServiceImpl.class);
	//analytics queue
	public static String SOURCE_QUEUE_1 = "qmanager:queue:emailQ";
	//logging queue
	public static String SOURCE_QUEUE_2 = "qmanager:queue:analQ";
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	
	public static void init(PatientRepository patientRepository) {
		
		logger.debug("Initializing cron job for the hazelcast cache");
		ExecutorService executor = Executors.newFixedThreadPool(1);
		UpdateCache uc = new UpdateCache(patientRepository);
		//if cron was never initiated start it.
		if (!CurrentProperties.isCronInitiated()) {
			CurrentProperties.setCronInitiatedStatus(true);
			try {
				executor.submit(uc);
			}
			catch(Exception ex) {
				logger.error("Error occured while initializing the cache: "+ex.getMessage());
			}
		}
			
	}
	
	public Patient getPatientDetailById(Integer id, PatientRepository patientRepository){
		
		logger.debug("Getting Patients for id " + id);
		
		//initialize cache if not done so far
		init(patientRepository);
		
		Patient p = null;
		
		//get from hcache first
		if(!CurrentProperties.getCurrentCacheStatus()) {
			Config cfg = new Config();
			HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
			IMap<Integer, Object> mapPatients = instance.getMap("patients");
			p = (Patient)mapPatients.get(id);
			logger.debug("Successfully retrieved patient from cache for id " +id);
		}
		
		//if not retrieved from cache
		if (p == null) {
			p =  PatientDaoService.getPatientById(id, patientRepository);
		}
		
		try {
		//sending message to activemq
			if (p != null) {
				ActiveMqMessage myMsg = new ActiveMqMessage();
				List<Patient> res = new ArrayList<>();
				res.add(p);
				myMsg.setType("GET");
				ObjectMapper mapper = new ObjectMapper();
				String msg = "Record retrieved with id: " + p.getId();
				String patient = mapper.writeValueAsString(res);
				myMsg.setMsg(msg); myMsg.setPatient(patient);
				logger.debug("logging activity in activemq: "+msg);
				MyActiveMq.sendMessage(SOURCE_QUEUE_1, mapper.writeValueAsString(myMsg));
				MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
			}
		}
		catch (Exception e ) {
			logger.error("Cannot serialize object:"+e.getMessage());
		}
		
		return p;
	}

	
	public Boolean createPatientRecord(Patient p, PatientRepository patientRepository) {
		
		//initialize cache if not done so far
		init(patientRepository);
		
		logger.debug("Creating new patient record");
		int newId = CurrentProperties.getLastPatientId();
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		p.setCreated_at(currentDate);
		p.setUpdated_at(currentDate);
		p.setId(newId);
		//update HazelCast configuration
		Config cfg = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		IMap<Integer, Object> mapPatients = instance.getMap("patients");
		mapPatients.put(p.getId(), (Patient)p);
		logger.debug("Successfully created a new record with id " + p.getId());
		CurrentProperties.setLastPatientId(newId+1);
		
		//update the cassandra db
		Boolean status = PatientDaoService.createNewPatient(p, patientRepository);
		
		try {
			//sending message to activemq
				if (p != null) {
					ActiveMqMessage myMsg = new ActiveMqMessage();
					List<Patient> res = new ArrayList<>();
					res.add(p);
					myMsg.setType("POST");
					ObjectMapper mapper = new ObjectMapper();
					String msg = "New Record created with id: " + p.getId();
					String patient = mapper.writeValueAsString(res);
					myMsg.setMsg(msg); myMsg.setPatient(patient);
					logger.debug("logging activity in activemq: "+msg);
					MyActiveMq.sendMessage(SOURCE_QUEUE_1, mapper.writeValueAsString(myMsg));
					MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
				}
			}
		catch (Exception e ) {
			logger.error("Cannot serialize object:"+e.getMessage());
		}
		
		return status;
	}

	@Override
	public List<Patient> getAllPatients(PatientRepository patientRepository) {
		
		//initialize cache if not done so far
		init(patientRepository);
		
		logger.debug("Retrieving all records");
		
		List<Patient> res = PatientDaoService.getAllPatients(patientRepository);
		
		try {
			//sending message to activemq
				if (res != null) {
					ActiveMqMessage myMsg = new ActiveMqMessage();
					myMsg.setType("GET");
					ObjectMapper mapper = new ObjectMapper();
					String msg = "Number of records retrieved: " + res.size();
					String patient = mapper.writeValueAsString(res);
					myMsg.setMsg(msg); myMsg.setPatient(patient);
					logger.debug("logging activity in activemq: "+msg);
					MyActiveMq.sendMessage(SOURCE_QUEUE_1, mapper.writeValueAsString(myMsg));
					MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
				}
			}
		catch (Exception e ) {
			logger.error("Cannot serialize object:"+e.getMessage());
		}
	
		logger.debug("All records retreived");
		return res;
	}

	@Override
	public Boolean deletePatientRecord(Integer id, PatientRepository patientRepository) {
		
		//initialize cache if not done so far
		init(patientRepository);
		
		logger.debug("Deleting record with id:" + id);
		
		//delete record from hazelcache
		if(!CurrentProperties.getCurrentCacheStatus()) {
			Config cfg = new Config();
			HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
			IMap<Integer, Object> mapPatients = instance.getMap("patients");
			if (mapPatients.containsKey(id)) {
				mapPatients.remove(id);
			}
			logger.debug("Successfully removed patient record from cache for id " +id);
		}
		
		try {
			//sending message to activemq
				ActiveMqMessage myMsg = new ActiveMqMessage();
				myMsg.setType("DELETE");
				ObjectMapper mapper = new ObjectMapper();
				String msg = "Record deleted with id: " + id;
				myMsg.setMsg(msg);
				logger.debug("logging activity in activemq: "+msg);
				MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
				
			}
			catch (Exception e ) {
				logger.error("Cannot serialize object:"+e.getMessage());
		}
		
		//delete record from the db
		return PatientDaoService.deleteRecord(id, patientRepository);
		
		
	}

	@Override
	public List<Patient> getPatientDetailByStatus(String status, PatientRepository patientRepository) {
		
		//initialize cache if not done so far
		init(patientRepository);
		
		logger.debug("Retrieve records with status:" + status);

		List<Patient> res = new ArrayList<>();
		
		//retrieve records from hazelcache
		if(!CurrentProperties.getCurrentCacheStatus() && CurrentProperties.getCurrentPolicy() == status) {
			Config cfg = new Config();
			HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
			IMap<Integer, Object> mapPatients = instance.getMap("patients");
			for (Integer i: mapPatients.keySet()) {
				res.add((Patient)mapPatients.get(i));
			}
			logger.debug("Successfully retrieved patient records from cache for status " +status);
		}
		
		//if no record in cache get from db
		if (res == null || res.isEmpty()) {
			logger.debug("Cache miss for status: " + status);
			res = PatientDaoService.getPatientByStatus(status, patientRepository);
		}
		

		try {
			//sending message to activemq
				if (res != null) {
					ActiveMqMessage myMsg = new ActiveMqMessage();
					myMsg.setType("GET");
					ObjectMapper mapper = new ObjectMapper();
					String msg = "Number of records retrieved based on status: " + status + " = " + res.size();
					String patient = mapper.writeValueAsString(res);
					myMsg.setMsg(msg); myMsg.setPatient(patient);
					logger.debug("logging activity in activemq: "+msg);
					MyActiveMq.sendMessage(SOURCE_QUEUE_1, mapper.writeValueAsString(myMsg));
					MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
				}
			}
		catch (Exception e ) {
			logger.error("Cannot serialize object:"+e.getMessage());
		}
		
		logger.debug("Successfully Retrieved records with status:" + status);
		return res;
	}

	@Override
	public Boolean updatePatientRecords(Patient p, PatientRepository patientRepository) {
		
		if (p.getId() == null) {
			logger.error("Id cannot be null to update a record");
			return false;
		}
			
		
		//initialize cache if not done so far
		init(patientRepository);
		
		logger.debug("updating record of patient with id:"+p.getId());
		
		//update the time 
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		p.setUpdated_at(currentDate);
		
		//update record in hazelcache
		if(!CurrentProperties.getCurrentCacheStatus()) {
			Config cfg = new Config();
			HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
			IMap<Integer, Object> mapPatients = instance.getMap("patients");
			if (mapPatients.containsKey(p.getId())) {
				Patient res = (Patient)mapPatients.get(p.getId());
				if (p.getName() != null && !p.getName().isEmpty())
					res.setName(p.getName());
				if (p.getAge() != null )
					res.setAge(p.getAge());
				if (p.getDischarged() != null)
					res.setDischarged(p.getDischarged());
				if (p.getDisease() != null && !p.getDisease().isEmpty())
					res.setDisease(p.getDisease());
				if (p.getEmail_id() != null && !p.getEmail_id().isEmpty())
					res.setEmail_id(p.getEmail_id());
				if (p.getRoom_no() != null)
					res.setRoom_no(p.getRoom_no());
				if (p.getStatus() != null && !p.getStatus().isEmpty())
					res.setStatus(p.getStatus());
				if (p.getUpdated_at() != null && !p.getUpdated_at().isEmpty())
					res.setUpdated_at(p.getUpdated_at());
				mapPatients.set(p.getId(), res);
			}
			logger.debug("Successfully update patient record in cache for id: "+p.getId());
		}
		
		//update the db
		Boolean status = PatientDaoService.updateRecord(p, patientRepository);
		logger.debug("DB record updated for patient id: "+p.getId());
		

		try {
			//sending message to activemq
				if (p != null) {
					ActiveMqMessage myMsg = new ActiveMqMessage();
					List<Patient> res = new ArrayList<>();
					res.add(p);
					myMsg.setType("PUT");
					ObjectMapper mapper = new ObjectMapper();
					String msg = "Record updated for patient with ID: " + p.getId();
					String patient = mapper.writeValueAsString(res);
					myMsg.setMsg(msg); myMsg.setPatient(patient);
					logger.debug("logging activity in activemq: "+msg);
					MyActiveMq.sendMessage(SOURCE_QUEUE_1, mapper.writeValueAsString(myMsg));
					MyActiveMq.sendMessage(SOURCE_QUEUE_2, mapper.writeValueAsString(myMsg));
				}
			}
		catch (Exception e ) {
			logger.error("Cannot serialize object:"+e.getMessage());
		}
		
		return status;
	}

}
