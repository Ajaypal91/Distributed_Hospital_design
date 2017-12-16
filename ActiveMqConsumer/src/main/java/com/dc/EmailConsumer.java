package com.dc;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailConsumer implements Processor {
	
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String payload = exchange.getIn().getBody(String.class);
		System.out.println("Message Received " + payload);
		ObjectMapper mapper = new ObjectMapper();
		ActiveMqMessage activeMqMessage = mapper.readValue(payload, ActiveMqMessage.class);
		
		List<Patient> patients = (List<Patient>) mapper.readValue(activeMqMessage.getPatient(), new TypeReference<List<Patient>>(){});

		
		for (Patient p : patients) {
			if (p.getEmail_id()  != null && !p.getEmail_id().isEmpty()) {
				final String emailId = p.getEmail_id();
				final String emailSubject = "Patient Information";
				final String emailBody = activeMqMessage.getMsg();
				
				//send email here
				
//				// Create the application context
		        ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/resources/ApplicationContext.xml");
//		        
		        // Get the mailer instance
		        ApplicationMailer mailer = (ApplicationMailer) context.getBean("mailService");
//		 
		        // Send a composed mail
		        mailer.sendMail(emailId, emailSubject, emailBody);
		        System.out.println("An email has been sent to " + emailId);
			}
		}
}
}
