package com.thalesgroup.optet.analysis.checkstyle;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.osgi.framework.Bundle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.checks.sizes.LineLengthCheck;
import com.puppycrawl.tools.checkstyle.XMLLogger;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle;
import com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File.Error;
import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.common.data.Severity;
import com.thalesgroup.optet.twmanagement.Evidence;
import com.thalesgroup.optet.twmanagement.Metric;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;



/**
 * checkstyleUtil checkstyle wrapper
 * @author F. Motte
 *
 */
public class CheckstyleUtil extends AuditToolUtil{
	/** A brief logger that only display errors */
	protected static class BriefLogger extends DefaultLogger
	{

		public BriefLogger(OutputStream out)
		{
			super(out, true);
		}

		@Override
		public void auditStarted(AuditEvent evt) {
		}

		@Override
		public void fileFinished(AuditEvent evt) {
		}

		@Override
		public void fileStarted(AuditEvent evt) {
		}
	}
	// the checkstyle preference
	private CheckStylePreferences pref;
	

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private final PrintStream printStream = new PrintStream(baos);
	

	/**
	 * constructor
	 * @param iproject
	 * @param files
	 */
	public CheckstyleUtil(IProject iproject, List<IFile> files) {
		super(iproject, files);
		// TODO Auto-generated constructor stub
		pref = new CheckStylePreferences();
	}

	public static File resolveFile(String path,Bundle bundle) throws IOException {
		  File file=null;
		  if (bundle != null) {
		    URL url=FileLocator.find(bundle,new Path(path),Collections.emptyMap());
		    if (url != null) {
		      URL fileUrl=FileLocator.toFileURL(url);
		      try {
		        file=new File(fileUrl.toURI());
		      }
		 catch (      URISyntaxException e) {
		        e.printStackTrace();
		      }
		    }
		  }
		  return file;
		}

	
	Configuration createCheckerConfig(String metric, String projectType)
	{
		
		Configuration dc = null;
//		final DefaultConfiguration dc =
//				new DefaultConfiguration("configuration");
//		final DefaultConfiguration twConf = new DefaultConfiguration("TreeWalker");
//		// make sure that the tests always run with this charset
//		dc.addAttribute("charset", "iso-8859-1");
//		dc.addChild(twConf);
		String ruleSetsPath = null;
		if (projectType.equals("java")){
			PluginHelper.getInstance().logInfo("Metrics " + metric);
			if (metric.equals("SecurityMetric")){
				OptetDataModel.getInstance().configureRulesMetric(metric, 0);
//			} else if (metric.equals("SoftwareComplexityMetric")){
//				twConf.addChild(new DefaultConfiguration("BooleanExpressionComplexity"));
//				twConf.addChild(new DefaultConfiguration("ClassDataAbstractionCoupling"));
//				twConf.addChild(new DefaultConfiguration("ClassFanOutComplexity"));
//				twConf.addChild(new DefaultConfiguration("ExecutableStatementCount"));
//				twConf.addChild(new DefaultConfiguration("FileLength"));
//				twConf.addChild(new DefaultConfiguration("LineLength"));
//				twConf.addChild(new DefaultConfiguration("OuterTypeNumber"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 7);
			}else if (metric.equals("interceptet errors")){
				
				Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
				URL fileURL = bundle.getEntry("resources/errors.xml");
				String file = null;
				try {
					file = FileLocator.resolve(fileURL).toURI().toString();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (file==null)
					try {
						file= CheckstyleUtil.resolveFile("resources/errors.xml", bundle).toString();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				ruleSetsPath = file;
				
				try {
					dc = ConfigurationLoader.loadConfiguration(ruleSetsPath, new PropertiesExpander(System.getProperties()));
				} catch (CheckstyleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				OptetDataModel.getInstance().configureRulesMetric(metric, 4);
//				twConf.addChild(new DefaultConfiguration("ThrowsCount"));
//				twConf.addChild(new DefaultConfiguration("IllegalCatch"));
//				twConf.addChild(new DefaultConfiguration("IllegalThrows"));
//				twConf.addChild(new DefaultConfiguration("MutableException"));
//				twConf.addChild(new DefaultConfiguration("RedundantThrows"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 5);
			}else if (metric.equals("Compliance with best programing practices")){
//				twConf.addChild(new DefaultConfiguration("BooleanExpressionComplexity"));
//				twConf.addChild(new DefaultConfiguration("ClassDataAbstractionCoupling"));
//				twConf.addChild(new DefaultConfiguration("ClassFanOutComplexity"));
//				twConf.addChild(new DefaultConfiguration("ExecutableStatementCount"));
//				twConf.addChild(new DefaultConfiguration("FileLength"));
//				twConf.addChild(new DefaultConfiguration("LineLength"));
//				twConf.addChild(new DefaultConfiguration("OuterTypeNumber"));
//				twConf.addChild(new DefaultConfiguration("ConstantName"));
//				twConf.addChild(new DefaultConfiguration("LocalFinalVariableName"));
//				twConf.addChild(new DefaultConfiguration("MemberName"));
//				twConf.addChild(new DefaultConfiguration("PackageName"));
//				twConf.addChild(new DefaultConfiguration("ParameterName"));
//				twConf.addChild(new DefaultConfiguration("StaticVariableName"));
//				twConf.addChild(new DefaultConfiguration("TypeName"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 14);
				
				Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
				URL fileURL = bundle.getEntry("resources/compliance.xml");
				String file = null;
				try {
					file = FileLocator.resolve(fileURL).toURI().toString();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (file==null)
					try {
						file= CheckstyleUtil.resolveFile("resources/compliance.xml", bundle).toString();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				ruleSetsPath = file;
				
				try {
					dc = ConfigurationLoader.loadConfiguration(ruleSetsPath, new PropertiesExpander(System.getProperties()));
				} catch (CheckstyleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OptetDataModel.getInstance().configureRulesMetric(metric, 14);

			}
		}
		else if (projectType.equals("android")){
			PluginHelper.getInstance().logInfo("Metrics " + metric);
			if (metric.equals("SecurityMetric")){
				OptetDataModel.getInstance().configureRulesMetric(metric, 0);
//			} else if (metric.equals("SoftwareComplexityMetric")){
//				twConf.addChild(new DefaultConfiguration("BooleanExpressionComplexity"));
//				twConf.addChild(new DefaultConfiguration("ClassDataAbstractionCoupling"));
//				twConf.addChild(new DefaultConfiguration("ClassFanOutComplexity"));
//				twConf.addChild(new DefaultConfiguration("ExecutableStatementCount"));
//				twConf.addChild(new DefaultConfiguration("FileLength"));
//				twConf.addChild(new DefaultConfiguration("LineLength"));
//				twConf.addChild(new DefaultConfiguration("OuterTypeNumber"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 7);
			}else if (metric.equals("interceptet errors")){
//				twConf.addChild(new DefaultConfiguration("ThrowsCount"));
//				twConf.addChild(new DefaultConfiguration("IllegalCatch"));
//				twConf.addChild(new DefaultConfiguration("IllegalThrows"));
//				twConf.addChild(new DefaultConfiguration("MutableException"));
//				twConf.addChild(new DefaultConfiguration("RedundantThrows"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 5);
			}else if (metric.equals("Compliance with best programing practices")){
//				twConf.addChild(new DefaultConfiguration("BooleanExpressionComplexity"));
//				twConf.addChild(new DefaultConfiguration("ClassDataAbstractionCoupling"));
//				twConf.addChild(new DefaultConfiguration("ClassFanOutComplexity"));
//				twConf.addChild(new DefaultConfiguration("ExecutableStatementCount"));
//				twConf.addChild(new DefaultConfiguration("FileLength"));
//				twConf.addChild(new DefaultConfiguration("LineLength"));
//				twConf.addChild(new DefaultConfiguration("OuterTypeNumber"));
//				twConf.addChild(new DefaultConfiguration("ConstantName"));
//				twConf.addChild(new DefaultConfiguration("LocalFinalVariableName"));
//				twConf.addChild(new DefaultConfiguration("MemberName"));
//				twConf.addChild(new DefaultConfiguration("PackageName"));
//				twConf.addChild(new DefaultConfiguration("ParameterName"));
//				twConf.addChild(new DefaultConfiguration("StaticVariableName"));
//				twConf.addChild(new DefaultConfiguration("TypeName"));
//				OptetDataModel.getInstance().configureRulesMetric(metric, 14);

			}	
		}
		return dc;
	}

	private void runSpecificAudit( String metric, String projectType){
		OutputStream xmlOutput = null;

		try {
			Configuration dc = createCheckerConfig(metric, projectType);
			xmlOutput = new ByteArrayOutputStream();
			AuditListener listener = new XMLLogger(xmlOutput, true);
			PluginHelper.getInstance().logInfo("listner1");
			Checker checker = new Checker();

			checker.setModuleClassLoader(Checker.class.getClassLoader());
			checker.configure(dc);
			PluginHelper.getInstance().logInfo("listner2");

			checker.addListener(listener);
			//checker.addListener(new BriefLogger(printStream));
			PluginHelper.getInstance().logInfo("listner3");

			checker.process(getFileList());
			PluginHelper.getInstance().logInfo("listner4");

//			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//			BufferedReader br = new BufferedReader(new InputStreamReader(bais));
//			
//			
//            int line = 0;
//            for (String x = br.readLine(); x != null; x = br.readLine())
//            {
//                line++;
//                System.out.println(x);
//                
//            }
			checker.destroy();
			PluginHelper.getInstance().logInfo("listner5");

			xmlOutput.close();
			PluginHelper.getInstance().logInfo(xmlOutput.toString());
			// transfer the checkstyle result into the optet data model
			checkstyleToOptet(xmlOutput.toString(),metric);
		} catch (Exception e) {


		} 
	}


	/* (non-Javadoc)
	 * @see eu.optet.audittools.AuditToolUtil#runAudit()
	 */
	public void runAudit(IProject project, List<Evidence> evidences, String projectType) throws Exception {

		for (Iterator iterator = evidences.iterator(); iterator.hasNext();) {
			Evidence evidence = (Evidence) iterator.next();
			Map<String, Metric> metrics = evidence.getMetrics();
			for(Entry<String, Metric> entry : metrics.entrySet()){
				String key = entry.getKey();
				runSpecificAudit( key, projectType);
			}
		}

		OrchestrationImpl.getInstance().staticAnalyseFinished(Activator.PLUGIN_ID);


	}


	private static Checker createChecker(Configuration configuration) {
		try {
			final Checker checker = new Checker();
			//checker.setModuleClassLoader( Checker.class.getClassLoader() );
			checker.configure( configuration );
			return checker;
		} catch ( final CheckstyleException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * checkstyleToOptet transfer the checkstyle result into the optet data model
	 * @param payload the checkstyle result
	 * @throws JAXBException
	 */
	private void checkstyleToOptet (String payload, String RuleSet) throws JAXBException
	{
		// unmarshall the string into a java model
		PluginHelper.getInstance().logInfo("JAXBContext.newInstance");
		JAXBContext jaxbContext = JAXBContext.newInstance(Checkstyle.class);
		PluginHelper.getInstance().logInfo("jaxbContext.createUnmarshaller();");
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		PluginHelper.getInstance().logInfo(" jaxbUnmarshaller.unmarshal");
		Checkstyle errors = (Checkstyle) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(payload.getBytes()));
		PluginHelper.getInstance().logInfo(" errors.getFile();");
		List<com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File> l = errors.getFile();
		PluginHelper.getInstance().logInfo(" l.iterator();");
		Iterator<com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File> i = l.iterator();
		int nbfile = 0;
		int nberror = 0;
		while(i.hasNext()){
			com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File f = (com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File) i.next();
			//PluginHelper.getInstance().logInfo("file" + f.getName());
			nbfile++;
			Iterator<Error> j = f.getError().iterator();
			while(j.hasNext()){
				nberror++;
				com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File.Error e = (com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle.Checkstyle.File.Error)j.next();

				//PluginHelper.getInstance().logInfo("error" + e.getMessage());
				//todo modifier la severity pour le add marker
				//PluginHelper.getInstance().logInfo("addmarker");
				//addMarker(getFile(f.getName()), e.getMessage(), e.getLine().intValue(), getSeverity(e.getSeverity()));
				PluginHelper.getInstance().logInfo("adderror" + " checkstyle " + RuleSet  + " " + e.getSource());
				OptetDataModel.getInstance().addError(getFile(f.getName()), 
						"checkstyle",
						e.getLine().intValue(),
						getSeverity(e.getSeverity()),
						RuleSet,
						e.getMessage(),
						e.getMessage(),
						e.getSource());


			}
		}
		PluginHelper.getInstance().logInfo("nbfile " + nbfile);
		PluginHelper.getInstance().logInfo("nberror " + nberror);
	}

	@Override
	public void runMetric(IProject project) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * getSeverity translate the checkstyle severity into the optet severity
	 * @param severity the checkstyle severity
	 * @return the severity 
	 */
	private Severity getSeverity(String severity)
	{
		Severity sev = null;
		switch (severity) {
		case "high":
			sev = Severity.HIGH;
			break;
		case "medium":
			sev = Severity.MEDIUM;
			break;
		case "low":
			sev = Severity.LOW;
			break;

		default:
			sev=Severity.HIGH;
			break;
		}

		return sev;
	}


	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.analysis.checkstyle.AuditToolUtil#runRuntimeAnalysis()
	 */
	@Override
	public void runRuntimeAnalysis(IProject project) throws Exception {
		// TODO Auto-generated method stub

	}

}