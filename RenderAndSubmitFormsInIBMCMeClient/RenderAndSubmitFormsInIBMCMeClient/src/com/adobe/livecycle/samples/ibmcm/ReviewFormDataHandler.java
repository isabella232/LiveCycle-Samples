/*
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated All Rights Reserved
 *
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the 
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a 
 * source other than Adobe, then your use, modification, or distribution of it requires the prior 
 * written permission of Adobe.
 */

package com.adobe.livecycle.samples.ibmcm;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.idp.services.BLOB;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_RenderNRFormsFromSubmittedDataIBMCM;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_RenderNRFormsFromSubmittedDataIBMCMServiceLocator;
import com.ibm.mm.beans.CMBConnection;

/**
 * Component to handle the Review Form Data action in wdk.
 * It is called when the user clicks on Review Data link for a Form (xdp) in Webtop menu.
 * It gathers the required information from the Documentum session and xdp object and
 * invokes  <code>RenderFormsFromSubmittedDataDocumentum</code> service via WebServices transport.
 * 
 * @author kmalik
 *
 */
public class ReviewFormDataHandler extends HttpServlet
{
	private static final long serialVersionUID = 3564339699433356794L;

    public void init(ServletConfig servletconfig)throws ServletException {
        super.init(servletconfig);
    }

    /**
     * Method to receive get requests from the web server
     * (Passes them onto the doPost method)
     * @param request The HttpServletRequest which contains the information submitted via get
     * @param response A response containing the required response data for this request
     **/

     public void doGet(HttpServletRequest request, HttpServletResponse response)
       throws ServletException, IOException
     {
         doProcess(request, response);
     }

     /**
     * Method to relieve and process Post requests from the web server
     * @param request The HttpServletRequest which contains the information submitted via post
     * @param response A response containing the required response data for this request
     **/

     public void doPost(HttpServletRequest request, HttpServletResponse response)
       throws ServletException, IOException
     {
         doProcess(request, response);
     }

     /**
      * This method handles the request from the client to render or submit a form
      * which is created through AdobeDocumentPolicy.
      *
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      */
     
     public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
     IOException
     {

         System.out.println("Enter ReviewFormDataHandlerServlet");

         try
         {
             // Get PID of Data XML.
             String pidOfXdpData = request.getParameter("document");
             
             // Session related work
             HttpSession httpSession = request.getSession();
             CMBConnection _cmbConnection = (CMBConnection) httpSession.getAttribute("connectionBean");

             // Obtain credentials and other useful information.
             String dataStoreName = _cmbConnection.getDatastore().datastoreName();
             String dscCredentialName = _cmbConnection.getUserid();
             String dscCredentialPass = _cmbConnection.getPassword();

             // Set service invocation credential information 
             SamplesConnectorforIBMCM_Processes_RenderNRFormsFromSubmittedDataIBMCMServiceLocator renderServiceLocator = new SamplesConnectorforIBMCM_Processes_RenderNRFormsFromSubmittedDataIBMCMServiceLocator();
             SamplesConnectorforIBMCM_Processes_RenderNRFormsFromSubmittedDataIBMCM renderClient = renderServiceLocator.getRenderNRFormsFromSubmittedDataIBMCM();
             ((javax.xml.rpc.Stub) renderClient)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, dscCredentialName);
             ((javax.xml.rpc.Stub) renderClient)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, dscCredentialPass);

             // Set the URL to which form is diverted when submitted.
             String submitUrl = null;

             System.out.println("Submit Uri = " + submitUrl);
             System.out.println("Invoking using WSDL");

             System.out.println("Arguments used at service invocation: " );
             System.out.println("\nDataStore Name: " + dataStoreName + "\nDSC User Name: " + dscCredentialName 
                 + "\nPID of XDP Data: " +  pidOfXdpData + "\nSubmit URL: " + submitUrl);

             // Service Invocation Notes:
             // [1] To prepopulate data in the rendered form:
             //      Pass a data document for 1st null parameter below
             System.out.println("Invoking RenderNRFormsFromSubmittedData-IBMCM Service.");
             BLOB renderedBlob = renderClient.invoke(dataStoreName, dscCredentialPass, dscCredentialName, 
                 pidOfXdpData, submitUrl);
             
             System.out.println("Pdf blob returned" + renderedBlob.toString());

             String remoteUrl = renderedBlob.getRemoteURL();
             System.out.println("BLOB: Remote URL:- " + remoteUrl);

             System.out.println("Redirect to remoteURL");
             response.sendRedirect(remoteUrl);

         }
         catch (Throwable fault)
         {
             System.out.println("Exception caught");
             fault.printStackTrace();
             throw new ServletException(fault);
         }

         System.out.println("Exit ReviewFormDataHandlerServlet");
     }
}
