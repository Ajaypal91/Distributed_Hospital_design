package com.dc.util;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.dc.component.Patient;

@Repository
public interface PatientRepository extends CassandraRepository<Patient>{

	@Query("SELECT*FROM patient WHERE id=?0 ALLOW FILTERING")
    Iterable<Patient> findById(Integer id);
	
	@Query("SELECT*FROM patient WHERE status=?0 ALLOW FILTERING")
	Iterable<Patient> findByStatus(String status);
	
	@Query("DELETE FROM patient WHERE id=?0")
	Iterable<Patient> deleteRecord(Integer id);
}
