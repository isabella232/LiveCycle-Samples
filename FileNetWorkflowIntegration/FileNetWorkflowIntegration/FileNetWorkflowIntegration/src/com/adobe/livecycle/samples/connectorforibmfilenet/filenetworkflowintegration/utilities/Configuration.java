/**
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated
 * All Rights Reserved
 * 
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a
 * source other than Adobe, then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities;


public class Configuration 
{
    // Constants
    private static final String CONFIGURATION_BUNDLE_NAME = "Configuration";
    private static final String LIVECYCLE_ES_BASE_URL = "LIVECYCLE_ES_BASE_URL";
    private static final String FILENET_ROUTER_URL = "FILENET_ROUTER_URL";
    private static final String SAMPLE_WORKFLOW_DEFINITION_FILE_LOCATION = "SAMPLE_WORKFLOW_DEFINITION_FILE_LOCATION";    

    private static java.util.ResourceBundle s_ConfigurationBundle = java.util.ResourceBundle.getBundle(CONFIGURATION_BUNDLE_NAME);
    private java.util.Properties m_configurationProps;    
    
    public Configuration()
    {
        m_configurationProps = new java.util.Properties();
        
        // Read resource bundle into properties
        java.util.Enumeration _keyEnumeration = s_ConfigurationBundle.getKeys();
        while (_keyEnumeration.hasMoreElements()) 
        {
            String _key = _keyEnumeration.nextElement().toString();
            String _value = (String) s_ConfigurationBundle.getObject(_key);  
            m_configurationProps.put(_key, _value);
        }
    }
    
    /** Returns value of property with given key */  
    public String getProperty(String aKey)
    {
        return m_configurationProps.getProperty(aKey);
    }

    /** Returns the LiveCycle Base URL */
    public String getLCBaseURL()
    {
        return getProperty(LIVECYCLE_ES_BASE_URL);
    }
    
    /** Returns the FileNet Router URL */
    public String getFileNetRouterURL()
    {
        return getProperty(FILENET_ROUTER_URL);
    }
    
    /** Returns the Sample Workflow Definition Location */
    public String getSampleWorkflowDefinitionFileLocation()
    {
        return getProperty(SAMPLE_WORKFLOW_DEFINITION_FILE_LOCATION);
    }
}