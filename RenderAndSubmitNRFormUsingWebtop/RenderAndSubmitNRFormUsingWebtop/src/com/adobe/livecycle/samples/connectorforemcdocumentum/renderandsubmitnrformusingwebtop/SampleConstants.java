/*
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated All Rights Reserved
 *
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the 
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a 
 * source other than Adobe, then your use, modification, or distribution of it requires the prior 
 * written permission of Adobe.
 */

package com.adobe.livecycle.samples.connectorforemcdocumentum.renderandsubmitnrformusingwebtop;

/**
 * Constants used in this sample.
 * 
 * @author kmalik
 *
 */
public class SampleConstants 
{
	//xml_link relation in Documentum
	public static final String RLN_XML_LINK 						= "xml_link";
    
    // Path Seperator used in Documentum
    public static final String PATH_SEPARATOR                              = "/";
	
	//Session attribute constants - for passing the values to SubmitFormServlet -
	public static final String SESS_ATTR_XDPPATH 					= "xdpPath";
	public static final String SESS_ATTR_XDPVERSION 				= "xdpVersion";
	public static final String SESS_ATTR_REPOSITORY 				= "repositoryName";
	public static final String SESS_ATTR_DCTM_USER 					= "dctmUserName";
	public static final String SESS_ATTR_DCTM_STOREDATA_TICKET	 	= "dctmStoreDataTicket";
	public static final String SESS_ATTR_DCTM_SET_LINK_TICKET 		= "dctmSetLinkTicket";
	public static final String SESS_ATTR_DCTM_CREATEFOLDER_TICKET 	= "dctmCreateFolderTicket";
	
	//Request attribute constants - for passing the values to jsp pages.
	public static final String REQ_ATTR_REMOTEURL 					= "remoteUrl";	
	public static final String REQ_ATTR_ERRTRACE					= "errorTrace";
	
	//Name of the servlet to be called on pressing submit button
	public static final String SUBMIT_FORM_SERVLET_NAME				= "AdobeSubmitFormService";
	
	//LiveCycle Connection property constants -
	public static final String CONNECTION_PROP_USERNAME				= "DSC_CREDENTIAL_USERNAME";
	public static final String CONNECTION_PROP_PASSWORD				= "DSC_CREDENTIAL_PASSWORD";
}
