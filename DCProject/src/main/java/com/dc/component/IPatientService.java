package com.dc.component;

import java.util.List;

import com.dc.util.PatientRepository;

public interface IPatientService {

	//get patient name by ID
	public Patient getPatientDetailById(Integer id,PatientRepository patientRepository);
	
	//save patient record
	public Boolean createPatientRecord(Patient p,PatientRepository patientRepository);
	
	//get all patients
	public List<Patient> getAllPatients(PatientRepository patientRepository);
	
	//delete patient record
	public Boolean deletePatientRecord(Integer id, PatientRepository patientRepository);
	
	//get records based on status
	public List<Patient> getPatientDetailByStatus(String status, PatientRepository patientRepository);
	
	//update the patient record
	public Boolean updatePatientRecords(Patient p,PatientRepository patientRepository);
	
	
}
