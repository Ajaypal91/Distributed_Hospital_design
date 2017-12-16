package com.dc.config;

import javax.servlet.ServletContext;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;  
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;  
import org.springframework.web.servlet.DispatcherServlet;
  
@Component
public class WebAppInitializer implements WebApplicationInitializer {
	
	public static AnnotationConfigWebApplicationContext ctx;
	public final static Logger logger = Logger.getLogger(WebAppInitializer.class);
	

	public void onStartup(ServletContext servletContext) throws ServletException {  
		logger.debug("Initializing Spring Context");
        ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);  
        ctx.setServletContext(servletContext);    
        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));  
        dynamic.addMapping("/");  
        dynamic.setLoadOnStartup(1);        
   }  
}
