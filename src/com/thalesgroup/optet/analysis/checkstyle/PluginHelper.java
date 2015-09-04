/*
 *		OPTET Factory
 *
 *	Class PluginHelper 1.0 7 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.analysis.checkstyle;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * @author F. Motte
 *
 */
public class PluginHelper {
	
	// Controls debugging of the plugin 
	public static boolean DEBUG;

	public ILog log;
	
	private static final PluginHelper INSTANCE = new PluginHelper();
	
	private PluginHelper() {
		super();
		
		Bundle bundle = Platform.getBundle(Platform.PI_RUNTIME);
		log = Platform.getLog(bundle);
		
	}

	public static PluginHelper getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Log an exception.
	 *
	 * @param e
	 *            the exception
	 * @param message
	 *            message describing how/why the exception occurred
	 */
	public void logException(Throwable e, String message) {
		logMessage(IStatus.ERROR, message, e);
	}

	/**
	 * Log an error.
	 *
	 * @param message
	 *            error message
	 */
	public void logError(String message) {
		logMessage(IStatus.ERROR, message, null);
	}

	/**
	 * Log a warning.
	 *
	 * @param message
	 *            warning message
	 */
	public void logWarning(String message) {
		logMessage(IStatus.WARNING, message, null);
	}

	/**
	 * Log an informational message.
	 *
	 * @param message
	 *            the informational message
	 */
	public void logInfo(String message) {
		logMessage(IStatus.INFO, message, null);
	}

	/**
	 * Log message 
	 * 
	 * @param severity	the severity of the message
	 * @param message	the message
	 * @param e 		the exception
	 */
	private void logMessage(int severity, String message, Throwable e) {
		if (DEBUG) {
			String what = (severity == IStatus.ERROR) ? (e != null ? "Exception" : "Error") : "Warning";
			System.out.println(what + " in Optet plugin: " + message);
			if (e != null) {
				e.printStackTrace();
			}
		}
		IStatus status = createStatus(severity, message, e);
		//Activator.getDefault().getLog().log(status);
		log.log(status);
	}

	/**
	 * create the status of the message 
	 * 
	 * @param severity the severity of the message
	 * @param message the message
	 * @param e the exception
	 * @return the created status
	 */
	private IStatus createStatus(int severity, String message, Throwable e) {
		return new Status(severity, Activator.PLUGIN_ID, 0, message, e);
	}
	
}
