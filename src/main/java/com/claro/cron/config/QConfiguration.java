package com.claro.cron.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//import com.claro.cron.jpa.service.CronService;

@Configuration
@PropertySource(value = { "file:${app.home}/config/application.properties" })
//@PropertySource(value = { "file:./config/application.properties" })
//@PropertySource(value = { "classpath:application.properties" })
public class QConfiguration {

	
//	
//	@Service
//	@Qualifier(value="cronservice")
//	CronService cronservice;
}
