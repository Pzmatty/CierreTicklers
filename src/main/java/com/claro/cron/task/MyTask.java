package com.claro.cron.task;

import java.net.MalformedURLException;

import org.springframework.stereotype.Component;


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
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

/**
 * This task counts from 1 to 30.
 */


public class MyTask extends Task {

	public boolean canBePaused() {
		return true;
	}

	public boolean canBeStopped() {
		return true;
	}

	public boolean supportsCompletenessTracking() {
		return true;
	}

	public boolean supportsStatusTracking() {
		return true;
	}


	
	public void execute(TaskExecutionContext context) throws RuntimeException {
//
//		try {
//			//JAXBExample.readXML();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
	}

	
	public void execute2(TaskExecutionContext context) throws RuntimeException {
		for (int i = 1; i <= 30; i++) {
			System.out.println("Task says: " +i);
			context.setStatusMessage("i = " + i);
			context.setCompleteness(i / 30D);
			System.out.println(" - paso de los toros -- "+i / 30D );
			try {
			//	System.out.println("- ----pausa de 1seg \n" );
				Thread.sleep(1000);
			//	System.out.println("- ----sale de pausa de 1seg" );
			} catch (InterruptedException e) {
				;
			}
			//System.out.println("- pause if requested" );
			context.pauseIfRequested();
			if (context.isStopped()) {
				System.out.println("is stopped");
				break;
			}
		}
	}	
	
	
}