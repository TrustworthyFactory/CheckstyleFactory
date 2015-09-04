package com.thalesgroup.optet.analysis.checkstyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;

import com.thalesgroup.optet.common.data.Severity;
import com.thalesgroup.optet.twmanagement.Evidence;



/**
 * AuditToolUtil audit tool wrapper
 * @author F. Motte
 *
 */
public abstract class AuditToolUtil {

	// the current project
	protected IProject iProject = null;

	/** Map containing the file resources to audit. */
	private Map<String, IFile> mFiles = new HashMap<String, IFile>();


	/**
	 * constructor
	 * @param iProject the current project
	 * @param files the current list of file to audit
	 */
	public AuditToolUtil(IProject iProject, List<IFile> files) {
		super();
		this.iProject = iProject;

		for (Iterator<IFile> iterator = files.iterator(); iterator.hasNext();) {
			IFile iFile = (IFile) iterator.next();
			addFile(iFile);
		}
	}

	/**
	 * run the audit on the selected files
	 * @param selection
	 * @throws Exception
	 */
	abstract public void runAudit(IProject project, List<Evidence> evidences, String projectType) throws Exception;

	/**
	 * run the metric on the selected files
	 * @param selection
	 * @throws Exception
	 */
	abstract public void runMetric(IProject project) throws Exception;

	/**
	 * run the runtime analysis 
	 * @param selection
	 * @throws Exception
	 */
	abstract public void runRuntimeAnalysis(IProject project) throws Exception;
	
	
	/**
	 * addfile add file into the current map file
	 * @param file a file
	 */
	private void addFile(IFile file)
	{
		//PluginHelper.getInstance().logInfo("register file " + file.getLocation().toOSString());
		mFiles.put(file.getLocation().toOSString(), file);
	}

	/**
	 * getFile return the IFile from the filename
	 * @param fileName the file name of the IFile
	 * @return the IFile
	 */
	protected IFile getFile(String fileName)
	{
		return (IFile) mFiles.get(fileName);
	}


	/**
	 * getFileList return the File list of the current selection
	 * @return return the File list 
	 */
	protected List<java.io.File> getFileList()
	{
		List<java.io.File> files = new ArrayList<File>();

		Collection<IFile> iFiles = mFiles.values();
		Iterator<IFile> it = iFiles.iterator();
		while (it.hasNext())
		{
			files.add(((IFile) it.next()).getLocation().toFile());
		}

		return files;
	}

	/**
	 * getIFileList return the IFile list of the current selection
	 * @return return the IFile list 
	 */
	protected List<IFile> getIFileList()
	{
		List<IFile> files = new ArrayList<IFile>();

		Collection<IFile> iFiles = mFiles.values();
		Iterator<IFile> it = iFiles.iterator();
		while (it.hasNext())
		{
			files.add(((IFile) it.next()));
		}

		return files;
	}


	/**
	 * addMarker add new marker into the the file
	 * @param file Ifile of the file
	 * @param message message for the marker
	 * @param lineNumber line for the marker
	 * @param severity severity for the maker
	 */
	protected void addMarker(IFile file, String message, int lineNumber,
			Severity severity) {
		try {
			// create maker
			IMarker marker = file.createMarker("optetMarker");
			//configure marker
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);


			switch (severity) {
			case HIGH:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				break;
			case MEDIUM:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				break;
			case LOW:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				break;
			default:
				break;
			}		
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}