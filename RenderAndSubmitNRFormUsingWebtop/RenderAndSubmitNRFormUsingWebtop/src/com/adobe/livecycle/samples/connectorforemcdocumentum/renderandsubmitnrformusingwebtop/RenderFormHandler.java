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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.Stub;

import com.adobe.idp.services.BLOB;
import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_RenderFormsFromNRDocumentum;
import com.adobe.idp.services.SamplesConnectorforEMCDocumentum_Processes_RenderFormsFromNRDocumentumServiceLocator;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.web.common.ArgumentList;
import com.documentum.web.form.Control;
import com.documentum.web.formext.component.Component;
import com.documentum.web.formext.session.SessionManagerHttpBinding;

/**
 * Component to handle the Render Form action in wdk.
 * It is called when the user clicks on Render Form link for a Form (xdp) in Webtop menu.
 * It gathers the required information from the Documentum session and xdp object and
 * invokes  <code>RenderReaderEnabledFormsWithDataDocumentum</code> service via WebServices transport.
 * <br>
 * It also populates the Html Session attributes to be used later for invoking 
 * <code>ArchiveSubmittedDataDocumentum</code> service.
 * 
 * @author kmalik
 *
 */
public class RenderFormHandler extends Component 
{

	private static final long serialVersionUID = -6062631498950094265L;

	public void onInit(ArgumentList args) 
	{
		System.out.println("ENTER RenderFormHandler Init");	
		
		super.onInit(args);
		
		//Retrieving the request and session -
		HttpServletRequest httpRequest = (HttpServletRequest) getPageContext().getRequest();
		HttpSession httpSession = getPageContext().getSession();

        IDfSessionManager sessionManager = null; 
        IDfSession session = null;
		
        
		try 
		{
			/*
			 * Specifying the jsp page to be opened after execution of this code.
			 */
			setComponentPage("start");
		    
			/*
			 * Forming Submit URL to be called on pressing the submit button on the rendered form - 
			 */
			String submitUrl = RequestUtil.getBaseUrl(httpRequest);
		    submitUrl = submitUrl +"/"+SampleConstants.SUBMIT_FORM_SERVLET_NAME;
			
		    /*
		     * Retrieving the Documentum session from Docbase and SessionManager, credentials and xdp sys-object information -
		     */
            sessionManager = SessionManagerHttpBinding.getSessionManager();
            String currentDocbase = SessionManagerHttpBinding.getCurrentDocbase();
            session = sessionManager.getSession(currentDocbase);
            
            String dctmUsername = session.getLoginInfo().getUser();
			String dctmStoreDataTicket = session.getLoginTicket();
            String dctmSetLinkTicket = session.getLoginTicket();
			String dctmCreateFolderTicket = session.getLoginTicket();
			String docbase 	= getCurrentDocbase();
			String objId = args.get("objectId");
			IDfSysObject sysObj = (IDfSysObject) session.getObject(new DfId(objId));
            String xdPathInLCRepository = getFormTemplateURLFromALO(sysObj);
//            String xdpName = null;
            String xdpFolderPathInLCRepository = null;
            
            if (xdPathInLCRepository != null)
            {
                xdpFolderPathInLCRepository = getXdpFolderPathFromURL(xdPathInLCRepository);
            }
            
			String xdpVersion = sysObj.getImplicitVersionLabel();
            
			
			/*
			 * Getting the LC credentials to be used for invoking the Web-service.
			 */
			Properties connectionProps = LCUtil.getServiceClientProps();
			String dscCredentialName = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_USERNAME);
			String dscCredentialPswd = connectionProps.getProperty(SampleConstants.CONNECTION_PROP_PASSWORD);
			
			/*
			 * For Service invocation via EJB call to LC Server -
			 */
//			Map inputParams = new HashMap();		    
//		    inputParams.put("submitUrl",submitUrl);
//		    inputParams.put("xdpDataDoc",null);
//          inputParams.put("xdpFolderPathInLCRepository",xdpFolderPathInLCRepository);
//		    inputParams.put("xdPathInLCRepository",xdPathInLCRepository);
//
//			Document renderedPDF = (Document) LCUtil.invokeLCService
//									(inputParams, connectionProps,"Samples - ConnectorforEMCDocumentum/Processes/RenderFormsFromNR-Documentum",
//									"invoke","readerEnabledPDF");
			
            SamplesConnectorforEMCDocumentum_Processes_RenderFormsFromNRDocumentumServiceLocator serviceLocater = 
		    	new SamplesConnectorforEMCDocumentum_Processes_RenderFormsFromNRDocumentumServiceLocator();
		    
            SamplesConnectorforEMCDocumentum_Processes_RenderFormsFromNRDocumentum service = 
		    	serviceLocater.getRenderFormsFromNRDocumentum();
		    
		    ((Stub)service)._setProperty(Stub.USERNAME_PROPERTY, dscCredentialName);
		    ((Stub)service)._setProperty(Stub.PASSWORD_PROPERTY, dscCredentialPswd);	
		    
		    System.out.println("Invoking the RenderFormsFromNRDocumentum service");
		    System.out.println("Parameters : " +
                                " xdpFolderPathInLCRepository = " + xdpFolderPathInLCRepository +
		    					" formTemplateURL = "+xdPathInLCRepository+
		    					" repositoryName = "+docbase+
		    					" submitUrl = "+submitUrl+
		    					" xdpDataDoc = null" );
		    BLOB renderedBlob = service.invoke(submitUrl, null, xdpFolderPathInLCRepository, xdPathInLCRepository);
		    System.out.println("Service Invocation successful");
		    String remoteUrl = renderedBlob.getRemoteURL();
		    System.out.println("Redirecting to  blob RemoteUrl = " + remoteUrl);
		    
		    /*
		     * Setting session attributes to be used by SubmitFormServlet while invoking
		     * ArchiveSubmittedDataDocumentum service.
		     */
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_XDPPATH		,xdPathInLCRepository);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_XDPVERSION	,xdpVersion);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_REPOSITORY	,docbase);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_DCTM_USER	,dctmUsername);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_DCTM_STOREDATA_TICKET,dctmStoreDataTicket);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_DCTM_SET_LINK_TICKET,dctmSetLinkTicket);
		    httpSession.setAttribute(SampleConstants.SESS_ATTR_DCTM_CREATEFOLDER_TICKET,dctmCreateFolderTicket);

		    /*
		     * Setting the request attribute to be used by jsp page after execution of this code.
		     */
		    httpRequest.setAttribute(SampleConstants.REQ_ATTR_REMOTEURL, remoteUrl);
		    
			System.out.println("EXIT RenderFormHandler Init");	
		} catch (Exception e) 
		{
		    /*
			 * Specifying the jsp page to be opened after execution of this code.
		     * Setting the request attribute to be used by jsp page after execution of this code.
		     */			
			setComponentPage("error");
			httpRequest.setAttribute(SampleConstants.REQ_ATTR_ERRTRACE, LCUtil.getStackTrace(e));
		}
		finally
		{
		    if (session != null && sessionManager != null)
		    {
		        sessionManager.release(session);
		    }
		}
	}

	public void onCancel(Control control,ArgumentList args)
	{
		setComponentReturn();
	}
    
    
    public String getFormTemplateURLFromALO(IDfSysObject sysObj) throws DfException, IOException
    {
        ArrayList dataBytesOfAssetObjectList = new ArrayList();
        ByteArrayInputStream iStream = sysObj.getContent();
        byte[] byteArray = new byte[1024*1024];
        while(true)
        {
            int len = iStream.read(byteArray);
            if(len==-1)
                break;
            for (int i = 0; i < len; i++)
            {
                dataBytesOfAssetObjectList.add(Byte.valueOf(byteArray[i]));
            }
        }

        byte[] dataBytesOfAssetObject = new byte[dataBytesOfAssetObjectList.size()];
        for (int i = 0; i < dataBytesOfAssetObject.length; i++)
        {
            dataBytesOfAssetObject[i] = ((Byte) dataBytesOfAssetObjectList.get(i)).byteValue();
        }

        return new String(dataBytesOfAssetObject);
    }

//    private String getXdpNameFromURL(String formTemplateURL)
//    {
//        int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(SampleConstants.PATH_SEPARATOR);
//        String formTemplateName = formTemplateURL.substring(lastIndexOfPathSeperator + 1, formTemplateURL
//        .length());
//        return formTemplateName;
//    }

    private String getXdpFolderPathFromURL(String formTemplateURL)
    {
        int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(SampleConstants.PATH_SEPARATOR);
        
        String formTemplateFolderPath = formTemplateURL.substring(0, lastIndexOfPathSeperator);
        return formTemplateFolderPath;
    }
}
