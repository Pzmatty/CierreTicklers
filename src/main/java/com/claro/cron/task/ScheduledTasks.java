package com.claro.cron.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.claro.cron.ws.client.RemedyClientImpl;

//import com.claro.cron.jpa.service.CronServiceImpl;


//@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	@Qualifier(value = "remedyClient")
	RemedyClientImpl service;
    
    //@Scheduled(fixedRate = 32000) // rate
    @Scheduled(cron = "0 * * * * ?") // every minute
	public void scheduleTaskWithFixedRate() {
		
		logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
		service.rmdSelectByStatus("");
	}

	
}
