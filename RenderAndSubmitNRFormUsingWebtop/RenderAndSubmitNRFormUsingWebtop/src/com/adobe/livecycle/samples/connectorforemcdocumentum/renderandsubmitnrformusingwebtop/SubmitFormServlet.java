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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_ArchiveSubmittedDataWithNRDocumentum;
import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_ArchiveSubmittedDataWithNRDocumentumServiceLocator;
import com.adobe.idp.services.BLOB;

/**
 * Servlet for invoking the <code>ArchiveSubmittedDataDocumentum</code> service
 * via WebServices transport.
 * <br> 
 * It retrieves the invocation arguments from the HttpSession attributes
 * set in the <code>RenderFormHandler</code>. It also constructs the Submitted data blob from 
 * Http Request.
 * 
 * @author kmalik
 *
 */
public class SubmitFormServlet extends HttpServlet 
{
	private static final long serialVersionUID = -3381222583701196820L;

	public void init(ServletConfig servletconfig)throws ServletException {
	  super.init(servletconfig);
	}

	public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
	   throws ServletException,IOException 
	   {
	  doProcess(httpservletrequest, httpservletresponse);
	}

	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
	    throws ServletException,IOException  
	{
	  doProcess(httpservletrequest, httpservletresponse);
	}

	/**
	 * This method handles the request to submit a form
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doProcess(HttpServletRequest request, HttpServletResponse response)
	   throws ServletException,IOException 
	   {
		System.out.println("ENTER SubmitFormServlet");	

	  try {
	   
		  /*
		   * Retrieving the environment and data buffers -
		   */
	    String envBuffer = RequestUtil.getEnvironBuffer(request);
	    byte[] formDatabuf = RequestUtil.getRequestBuffer(request);
	    
	    
	    /*
	     * Retrieving the session attributes -
	     */
	    HttpSession httpSession = request.getSession();
	    String xdpPath = (String)httpSession.getAttribute(SampleConstants.SESS_ATTR_XDPPATH);
	    String repositoryName = (String)httpSession.getAttribute(SampleConstants.SESS_ATTR_REPOSITORY);
        String dctmUsername =(String)httpSession.getAttribute(SampleConstants.SESS_ATTR_DCTM_USER);
	    String dctmStoreDataTicket =(String) httpSession.getAttribute(SampleConstants.SESS_ATTR_DCTM_STOREDATA_TICKET);
	    String dctmSetLinkTicket =(String) httpSession.getAttribute(SampleConstants.SESS_ATTR_DCTM_SET_LINK_TICKET);
	    String dctmCreateFolderTicket =(String) httpSession.getAttribute(SampleConstants.SESS_ATTR_DCTM_CREATEFOLDER_TICKET);
	    String xmlDataPath = "/LiveCycleES/ConnectorforEMCDocumentum/ArchiveSubmittedDataWithNR-Documentum/Data";

	    /*
	     * Removing the session attributes -
	     */
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_XDPPATH);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_XDPVERSION);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_REPOSITORY);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_DCTM_USER);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_DCTM_STOREDATA_TICKET);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_DCTM_SET_LINK_TICKET);
	    httpSession.removeAttribute(SampleConstants.SESS_ATTR_DCTM_CREATEFOLDER_TICKET);
	    
	    /*
	     * Generating filename for xml data file -
	     */
	    String xdpName = xdpPath.substring(xdpPath.lastIndexOf("/")+1);
	    if(xdpName.indexOf(".")!=-1)
	    {
	      xdpName = xdpName.substring(0,xdpName.indexOf("."));
	    }
	    //String xmlDataFileName = "FormData_"+xdpName +"_"+dctmUsername +"_"+ LCUtil.getCurrentDateAsString()+".xml";
//	    String xmlDataFileName = xdpName; 
	    	
	    //Creating the Submitted Data BLOB from data buffer -
	    BLOB submittedDataBlob = new BLOB();
	    submittedDataBlob.setBinaryData(formDatabuf);
	    
		/*
		 * Getting the LC credentials to be used for invoking the Web-service.
		 */	    
		Properties connectionProps = LCUtil.getServiceClientProps();
		String dscCredentialName = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_USERNAME);
		String dscCredentialPswd = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_PASSWORD);
	    
        SamplesConnectorforEMCDocumentum_Processes_ArchiveSubmittedDataWithNRDocumentumServiceLocator serviceLocator = 
	    	new SamplesConnectorforEMCDocumentum_Processes_ArchiveSubmittedDataWithNRDocumentumServiceLocator();
	    
        SamplesConnectorforEMCDocumentum_Processes_ArchiveSubmittedDataWithNRDocumentum service = serviceLocator.getArchiveSubmittedDataWithNRDocumentum();
	    
	    ((javax.xml.rpc.Stub)service)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, dscCredentialName);
	    ((javax.xml.rpc.Stub)service)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, dscCredentialPswd);
	    
	    System.out.println("Invoking the ArchiveSubmittedDataDocumentum service");

    /*
     * We can specify either Documentum Password or login tickets in arguments.
     * Since from within webtop, only login tickets are available, so we are passing login tickets.
     * The login tickets can be used only once. 
     * Since in the orchestration ArchiveSubmittedData-Documentum, two operations of Documentum Content Repository Connector
     * are used, a Documentum ticket is required for each of them separately. 
     * Thus, two tickets are needed - one for Create Relation operation, another for Store Data operation.
     * 
     * Also, the relationship type can be used anything but it should be valid and consistent with the handling of 
     * Submitted data file later (ReviewFormDataHandler class).
     */
	    	  
	    service.invoke(xmlDataPath, dctmCreateFolderTicket, null, dctmSetLinkTicket, dctmStoreDataTicket, dctmUsername,
	    envBuffer, repositoryName, submittedDataBlob, xdpPath);
	    
	    System.out.println("Service Invocation successful");
	    
	    /*
	     * Code for closing the Form window after pressing the submit button.
	     */
	      PrintWriter pw =response.getWriter();
	      pw.println("<html>");
	      pw.println("<head></head>");
	      pw.println("<body>");
	      pw.println("<script>window.close();</script>");
	      pw.println("</body>");
	      pw.println("</html>");
	      
	      System.out.println("EXIT SubmitFormServlet");
	      
	      pw.flush();
	  
	  }catch(Throwable fault)
	  {
	    System.out.println("Exception caught");
	    fault.printStackTrace();
	    throw new ServletException(fault);
	  }

	}



}
