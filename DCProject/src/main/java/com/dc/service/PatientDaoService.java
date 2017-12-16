package com.dc.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dc.component.Patient;
import com.dc.util.PatientRepository;
import com.google.common.base.Objects;

@Repository
public class PatientDaoService{
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private final static Logger logger = Logger.getLogger(PatientDaoService.class);
	private static String filteredDate = "1999/12/31 24:00:00";
	
	public static List<Patient> getAllPatients(PatientRepository patientRepository){
		
		List<Patient> res = new ArrayList<>();
		List<Patient> resultSet = new ArrayList<>();
		try {
			Date refDate = formatter.parse(filteredDate);
			patientRepository.findAll().forEach(e-> res.add(e));
			for(Patient p : res) {
				if (refDate.before(formatter.parse(p.getCreated_at())))
					resultSet.add(p);
			}
		} catch (ParseException e1) {
			logger.error("Cannot parse the date:"+e1.getMessage());
		}
		
		return resultSet;
	}
	
	public static Patient getPatientById(Integer id, PatientRepository patientRepository) {
		List<Patient> res = new ArrayList<>();
		patientRepository.findById(id).forEach(e->res.add(e));
		return res.get(0);
	}
	
	public static List<Patient> getPatientByStatus(String status, PatientRepository patientRepository){
		
		List<Patient> res = getAllPatients(patientRepository);
		List<Patient> finalres = new ArrayList<>();
		logger.debug("Records retrieved from db, size:" + res.size());
		for (Patient p : res) {
			logger.debug("Status of result: "+p.getStatus());
			if (status.equals(p.getStatus())) {
				finalres.add(p);
			}
		}
		logger.debug("Records with status retrieved from db, size:" + finalres.size());
		return finalres;
	}
	
	public static Boolean createNewPatient(Patient p, PatientRepository patientRepository) {
		patientRepository.save(p);
		return true;
	}
	
	public static Boolean deleteRecord(Integer id, PatientRepository patientRepository) {
		patientRepository.deleteRecord(id);
		return true;
	}
	
	public static Boolean updateRecord(Patient p, PatientRepository patientRepository) {
		
		Patient res = getPatientById(p.getId(), patientRepository);
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
		deleteRecord(p.getId(), patientRepository);
		patientRepository.save(res);
		return true;
	}
}
