package com.claro.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import com.claro.cron.jpa.service.CronService;
import com.claro.cron.ws.client.RemedyClientImpl;

import ch.qos.logback.classic.Level;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableConfigurationProperties
//@EnableScheduling
public class QuartzApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(QuartzApplication.class);	
	
	@Autowired
	@Qualifier(value = "closeTicklerService")
	RemedyClientImpl service;

	@Value("${loglevel}")
	private String loglevel;	
	
	public static void main(String[] args) {
	try {	
		SpringApplication.run(QuartzApplication.class, args);
	} catch (Exception e)
	{
		System.out.println("error: "+e.getMessage());
	}
	}

	@Override
	public void run(String... args) throws Exception {
		setLogLevel(loglevel);
		service.rmdSelectByStatus("NP");
	}
	
	
	public static String setLogLevel(String logLevelP) {
        String retVal;
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        if ("DEBUG".equalsIgnoreCase(logLevelP)) {
        	logger.info("log level debug "+logLevelP);
            root.setLevel(Level.DEBUG);
            retVal = "ok";
        } else if ("INFO".equalsIgnoreCase(logLevelP)) {
        	logger.info("log level info "+logLevelP);
        	root.setLevel(Level.INFO);
            retVal = "ok";
        } else if ("TRACE".equalsIgnoreCase(logLevelP)) {
        	logger.info("log level trace "+logLevelP);
            root.setLevel(Level.TRACE);
            retVal = "ok";
        } else if ("ALL".equalsIgnoreCase(logLevelP)) {
        	logger.info("log level all "+logLevelP);
            root.setLevel(Level.ALL);
            retVal = "ok";
        } else {
            logger.error("Not a known loglevel: " + logLevelP);
            retVal = "Error, not a known loglevel: " + logLevelP;
        }
        return retVal;
    }}

