package com.dc.postprocessing;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.dc.config.WebAppInitializer;


public class MyActiveMq {
	
	@Autowired
	private static WebAppInitializer context;
	
	public static boolean sendMessage(String queue, String message) {
		
//		ApplicationContext context = new ClassPathXmlApplicationContext("/resources/appContext.xml");
        ProducerTemplate camelTemplate = context.ctx.getBean("camelTemplate", ProducerTemplate.class);
        System.out.println("Message Sending started");
        camelTemplate.sendBody(queue,message);
        System.out.println("Message sent");
		
		return true;
	}

}
