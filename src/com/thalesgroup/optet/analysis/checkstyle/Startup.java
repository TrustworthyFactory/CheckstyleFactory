/*
 *		OPTET Factory
 *
 *	Class Startup 1.0 7 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.analysis.checkstyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;





import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;
import com.thalesgroup.optet.plugin.template.jaxb.OptetPlugin;
/**
 * @author F. Motte
 *
 */
public class Startup implements IStartup{

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		PluginHelper.getInstance().logInfo("********************************** startup" + Activator.PLUGIN_ID);
		BundleContext ctx = Activator.getDefault().getBundle().getBundleContext();
		registerPlugin();
		registerEventHandler(ctx);
	}
	
	/**
	 * 
	 */
	private void registerPlugin() {
		
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry("xml/evidences.xml");
		File file = null;
		try {
			file = new File(FileLocator.toFileURL(fileURL).getPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(OptetPlugin.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			OptetPlugin conf = (OptetPlugin) jaxbUnmarshaller.unmarshal(file);	
			// TODO Auto-generated method stub
			OrchestrationInterface orchestrator = OrchestrationImpl.getInstance();
			orchestrator.registerPlugin(Activator.getDefault().PLUGIN_ID, conf, null);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param ctx
	 */
	private void registerEventHandler(BundleContext ctx) {
		EventHandler handler = new OptetEventHandler();

		PluginHelper.getInstance().logInfo("start checkstyle comm");

		Dictionary<String,String> properties = new Hashtable<String, String>();
		properties.put(EventConstants.EVENT_TOPIC, "viewcommunication/*");
		ctx.registerService(EventHandler.class, handler, properties);
		PluginHelper.getInstance().logInfo("start checkstyle comm");
	}


}
