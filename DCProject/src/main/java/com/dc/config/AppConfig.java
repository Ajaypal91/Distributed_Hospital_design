package com.dc.config;  
  
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.dc.util.PatientRepository;
  
@Configuration 
@ComponentScan("com.dc") 
@ImportResource("/WEB-INF/appContext.xml")
@EnableWebMvc   
public class AppConfig {  

}  
