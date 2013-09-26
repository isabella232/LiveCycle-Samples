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

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.Stub;

import com.adobe.idp.services.BLOB;
import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_RenderNRFormsFromSubmittedDataDocumentum;
import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_RenderNRFormsFromSubmittedDataDocumentumServiceLocator;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;
import com.documentum.web.common.ArgumentList;
import com.documentum.web.form.Control;
import com.documentum.web.formext.component.Component;
import com.documentum.web.formext.session.SessionManagerHttpBinding;

/**
 * Component to handle the Review Form Data action in wdk.
 * It is called when the user clicks on Review Data link for a Form (xdp) in Webtop menu.
 * It gathers the required information from the Documentum session and xdp object and
 * invokes  <code>RenderFormsFromSubmittedDataDocumentum</code> service via WebServices transport.
 * 
 * @author kmalik
 *
 */
public class ReviewFormDataHandler extends Component 
{
	private static final long serialVersionUID = 3564339699433352094L;

	public void onInit(ArgumentList args) 
	{
		System.out.println("ENTER ReviewFormDataHandler Init");	
		
		super.onInit(args);
		
		//Retrieving the request -
		HttpServletRequest httpRequest = (HttpServletRequest) getPageContext().getRequest();

        IDfSessionManager sessionManager = null;
		IDfSession session = null;
		try
		{
			/*
			 * Specifying the jsp page to be opened after execution of this code.
			 */			
			setComponentPage("start");
			
		    /*
		     * Retrieving the Documentum session from Docbase and SessionManager,credentials and xml data sys-object information -
		     */
			
			
			
            sessionManager = SessionManagerHttpBinding.getSessionManager();
            String currentDocbase = SessionManagerHttpBinding.getCurrentDocbase();
            session = sessionManager.getSession(currentDocbase);
            
            String dctmUsername = session.getLoginInfo().getUser();
			String dctmGetLinksTicket = session.getLoginTicket();
			String dctmRtrvDataTicket = session.getLoginTicket();
            String docbase  = getCurrentDocbase();
			
			String objId = args.get("objectId");
			IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objId));			
			String xmlDataName = sysObj.getObjectName();
			IDfId folderID=sysObj.getFolderId(0);
			IDfFolder folder=(IDfFolder)session.getObject(folderID);
			String folderPath=folder.getFolderPath(0);		    
			String xmlDataPath = folderPath+"/"+xmlDataName;
			
			/*
			 * No Submit URL for the submitted data form.
			 * 
			 * We could also have specified a separate servlet to be invoked on submit -
			 * String submitUrl = RequestUtil.getBaseUrl(httpRequest);
			 * submitUrl = submitUrl +"/"+"newServletName";
			 */
			String submitUrl = null;			
			
			
			/*
			 * Getting the LC credentials to be used for invoking the Web-service.
			 */			
			Properties connectionProps = LCUtil.getServiceClientProps();
			String dscCredentialName = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_USERNAME);
			String dscCredentialPswd = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_PASSWORD);
			
            SamplesConnectorforEMCDocumentum_Processes_RenderNRFormsFromSubmittedDataDocumentumServiceLocator locator = new SamplesConnectorforEMCDocumentum_Processes_RenderNRFormsFromSubmittedDataDocumentumServiceLocator();
			
            SamplesConnectorforEMCDocumentum_Processes_RenderNRFormsFromSubmittedDataDocumentum service = locator.getRenderNRFormsFromSubmittedDataDocumentum();
			
		    ((Stub)service)._setProperty(Stub.USERNAME_PROPERTY, dscCredentialName);
		    ((Stub)service)._setProperty(Stub.PASSWORD_PROPERTY, dscCredentialPswd);	
		    
		    System.out.println("Invoking the service");
		    
		    /*
		     * We can specify either Documentum Password or login tickets in arguments.
		     * Since from within webtop, only login tickets are available, so we are passing login tickets.
		     * The login tickets can be used only once. 
		     * Since in the orchestration RenderFormsFromSubmittedData-Documentum, four operations of 
		     * Documentum Content Repository Connector are used, a Documentum ticket is required for each of them separately. 
		     * Thus, four tickets are needed - one for Get Related operation, two for Retrieve Data operations(data and xdp)
		     * and one for Store Data operation.
		     * 
		     * Also, the relationship type should be the same as 
		     * when the relation was first created between the xdp and xml files.
		     */		    
		    BLOB renderedBlob = 
		    service.invoke(dctmGetLinksTicket, null, dctmRtrvDataTicket, dctmUsername, docbase, submitUrl, xmlDataPath);
		    
		    System.out.println("Service Invocation successful");

		    String remoteUrl = renderedBlob.getRemoteURL();
		    System.out.println("Redirecting to  blob RemoteUrl = " + remoteUrl);
		    
		    /*
		     * Setting the request attribute to be used by jsp page after execution of this code.
		     */		    
		    httpRequest.setAttribute(SampleConstants.REQ_ATTR_REMOTEURL, remoteUrl);
		    
			System.out.println("EXIT ReviewFormDataHandler Init");	
		    
		}
		catch(Exception e)
		{
		    /*
			 * Specifying the jsp page to be opened after execution of this code.
		     * Setting the request attribute to be used by jsp page after execution of this code.
		     */						
			setComponentPage("error");			
			httpRequest.setAttribute(SampleConstants.REQ_ATTR_REMOTEURL, LCUtil.getStackTrace(e));
						
		}
		finally
		{
			if(session!=null && sessionManager != null)
			{
					sessionManager.release(session);
			}			
		}
	}	
	
	public void onCancel(Control control,ArgumentList args)
	{
		setComponentReturn();
	}
}
