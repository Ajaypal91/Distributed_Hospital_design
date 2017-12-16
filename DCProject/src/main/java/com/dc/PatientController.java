package com.dc;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.component.IPatientService;
import com.dc.component.Patient;
import com.dc.service.PatientDaoService;
import com.dc.util.PatientRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/data")
public class PatientController {

	@Autowired 
	IPatientService patientService;
	
	@Autowired
	PatientRepository patientRepository;
	
	public final static Logger logger = Logger.getLogger(PatientController.class);
	
	
	/**
	 * API to get the patient records based on ID
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/patient", method = RequestMethod.GET, params="id")
	public Patient getPatientDetailsById(@RequestParam(value = "id") Integer id) {
		logger.debug("Get request received for id " + id);
		Patient p = patientService.getPatientDetailById(id,patientRepository);
		return p;		
	}
	
	/**
	 * API to get the patient records based on ID
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/patient/status", method = RequestMethod.GET, params="status")
	public List<Patient> getPatientDetailsByStatus(@RequestParam(value = "status") String status) {
		logger.debug("Get request received for status " + status);
		List<Patient> res = patientService.getPatientDetailByStatus(status,patientRepository);
		return res;		
	}
	
	
	/***
	 * API to get all patients from the db
	 * @return
	 */
	@RequestMapping(value = "/patient/all", method = RequestMethod.GET)
	public List<Patient> getAllPatients() {
		logger.debug("Get all patients request received");
		List<Patient> p = patientService.getAllPatients(patientRepository);
		return p;		
	}
	
	/**
	 * POST request to create a new patient record
	 * @param person
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/patient/new", produces = "application/json", consumes = "application/json")
	public Boolean createPatientRecord(@RequestBody Patient p) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Post request received");
		Boolean status = patientService.createPatientRecord(p, patientRepository);
		return status;		
	}
	
	/***
	 * API to delete patient from the db and cache
	 * @return
	 */
	@RequestMapping(value = "/patient/delete", method = RequestMethod.DELETE, params="id")
	public Boolean removePatientRecord(@RequestParam(value="id")Integer id) {
		logger.debug("Delete request received");
		return patientService.deletePatientRecord(id, patientRepository);
	}
	
	/***
	 * API to update patient from the db and cache
	 * @return
	 */
	@RequestMapping(value = "/patient/update", method = RequestMethod.PUT, consumes = "application/json")
	public Boolean updateRecords(@RequestBody Patient p) {
		logger.debug("Update request received");
		return patientService.updatePatientRecords(p, patientRepository);
	}
	
}

