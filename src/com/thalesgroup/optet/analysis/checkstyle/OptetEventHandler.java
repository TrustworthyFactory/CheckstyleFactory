/*
 *		OPTET Factory
 *
 *	Class OptetEventHandler 1.0 30 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.analysis.checkstyle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.thalesgroup.optet.twmanagement.Evidence;

/**
 * @author F. Motte
 *
 */
public class OptetEventHandler implements EventHandler {
	public void handleEvent(final Event event) {
		
		PluginHelper.getInstance().logInfo("new event = " + event.getTopic() + " " + event.getProperty("DATA"));


		IProject analysedProject = (IProject)(event.getProperty("project")); 
		PluginHelper.getInstance().logInfo(analysedProject.getName());
		List<IFile> filesToAnalyse = (List<IFile>)(event.getProperty("sources"));
		//ISelection selection = (ISelection)(event.getProperty("selection"));

		List<Evidence> evidences = (List<Evidence>)(event.getProperty("evidences"));
		String projectType = (String) event.getProperty("projectType");

		//PluginHelper.getInstance().logInfo("evidence size " + evidences.size());
		
		
		if (event.getTopic().equals("viewcommunication/staticAnalyse")){
			PluginHelper.getInstance().logInfo("checkstyle static analysis");
			CheckstyleUtil checkstyle = new CheckstyleUtil(analysedProject, filesToAnalyse);
			try {
				checkstyle.runAudit(analysedProject, evidences, projectType);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (event.getTopic().equals("viewcommunication/runtimeAnalyse")){
			PluginHelper.getInstance().logInfo("checkstyle runtime analysis");

			CheckstyleUtil checkstyle = new CheckstyleUtil(analysedProject, filesToAnalyse);
			try {
				checkstyle.runRuntimeAnalysis(analysedProject);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		PluginHelper.getInstance().logInfo("end event = " + event.getTopic() + " " + event.getProperty("DATA"));
	}
}
