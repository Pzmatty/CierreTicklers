package com.claro.cron.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//import com.claro.cron.jpa.service.CronService;

/*
 * cron4j - A pure Java cron-like scheduler
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.TaskExecutor;
@Component
public class Main {

//	@Autowired
//	@Qualifier(value = "cronservice")
//	CronService service;
	
	//@PostConstruct
	public void runTask(){
		mainTask();
	}
	
	public static void mainTask() {
		// Prepares the task.
		MyTask task = new MyTask();
		// Creates the scheduler.
		Scheduler scheduler = new Scheduler();
		// Schedules the task, once every minute.
		scheduler.schedule("* * * * *", task);
		// Starts the scheduler.
		System.out.println("************** comienza tarea");
		scheduler.start();
		TaskExecutor[] k = scheduler.getExecutingTasks();
		
		while(k.length==0)
		{
		//	System.out.println("estatus: "+k[0].getStatusMessage());	
			k = scheduler.getExecutingTasks();
		}
		System.out.println("************************************* estatus: "+k.length);
		
		
		long elapsedTime = k[0].getStartTime();
		String format = String.format("%%0%dd", 2);
	    elapsedTime = elapsedTime / 1000;
	    String seconds = String.format(format, elapsedTime % 60);
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);
	    String hours = String.format(format, elapsedTime / 3600);
	    String time =  hours + ":" + minutes + ":" + seconds;
	    
	    System.out.println("esta vivo? "+k[0].getStatusMessage());
	    
	    System.out.println("start time: "+time);
		// Stays alive for five minutes.
		try {
			//k[0].stop();
			System.out.println("///////////////// espera 5 minutos");
			Thread.sleep(5L * 60L * 1000L);
			//Thread.sleep(9500L);
			System.out.println("//////////////// paso 5 minutos");
		} catch (InterruptedException e) {
			;
		}
		// Stops the scheduler.
		scheduler.stop();
	}

}