package com.dc;

import java.io.FileOutputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class AnalyticsConsumer implements Processor {
	public void process(Exchange exchange) throws Exception {
		String payload = exchange.getIn().getBody(String.class);
		System.out.println("Message Received " + payload);
		
		// Write payload received from AnalyticsQueue in a text file
	    try {
	    	System.out.println("Creating file patientData.txt to store data consumed from AnalyticsQueue");
	    	FileOutputStream file = new FileOutputStream("patientData.txt");
	    		
	        byte[] payloadInBytes = payload.getBytes();
	    	file.write(payloadInBytes);
	    	System.out.println("Data has been written to the file");
	    	file.close();
	    } catch (Exception ex) {
	    	System.out.println("Exception while writing data to a file: " + ex);
	    }
	}
}
