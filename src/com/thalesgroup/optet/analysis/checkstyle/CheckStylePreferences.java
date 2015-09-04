package com.thalesgroup.optet.analysis.checkstyle;


/**
 * The checkstyle preference access
 * @author F. Motte
 *
 */
public class CheckStylePreferences {

	/**
	 * isCheckStyleUsed return true if checkstyle must be used from the preference choice
	 * @return true if checkstyle must be used from the preference choice
	 */
	public boolean isCheckStyleUsed() {
		return true;
	}
	
	
	/**
	 * auditRuleSetUsed return the checkstyle configuration file
	 * @return the configuration file
	 */
	public String getConfigurationFile () {
		return "";
	}
}
