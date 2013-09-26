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
package com.adobe.livecycle.samples.ibmcm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adobe.idp.services.BLOB;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_ArchiveSubmittedDataWithNRIBMCM;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_ArchiveSubmittedDataWithNRIBMCMServiceLocator;
import com.adobe.livecycle.samples.ibmcm.eClient.util.RequestUtil;


public class SubmitFormServlet extends HttpServlet {
    
    private static final long serialVersionUID = 3564339678657456794L;
    
    //Item Type Specific to Adobe XDP and Data XML. 
    public static String FORM_DATA_ITEM_TYPE = "Adobe_Data";
    public static String ALO_OBJECT_ITEM_TYPE = "AssetObjectType";

    // Mime Type of Adobe XML
    public static String FORM_DATA_MIME_TYPE = "webapps-x-adobe-submitteddata";
    
    /**
     * Initialize method to AdobeDPConfirmPrompt layout module.
     * @throws ServletException
     */
    public void init(ServletConfig servletconfig)throws ServletException {
        super.init(servletconfig);
    }

    /**
     * This method processes the doPost http request
     * @throws ServletException
     */
    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    throws ServletException,IOException {
        doProcess(httpservletrequest, httpservletresponse);
    }

    /**
     * This method processes the doGet http request
     * @throws ServletException
     */
    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    throws ServletException,IOException  {
        doProcess(httpservletrequest, httpservletresponse);
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
    public void doProcess(HttpServletRequest request, HttpServletResponse response)
    throws ServletException,IOException {
        
        System.out.println("Enter SubmitForm Servlet ");
        
        try {
            
            String environmentVariables = RequestUtil.getEnvironBuffer(request);
            byte[] formDatabuf = RequestUtil.getRequestBuffer(request);

            System.out.println("FormDataBuffer Size:- ");
            System.out.println(formDatabuf.length);
            
            // Do all session attribute handling
            HttpSession httpSession = request.getSession();
            String formTemplateURL = (String)httpSession.getAttribute("formTemplateURL");
            String dataStoreName = (String)httpSession.getAttribute("dataStoreName");
            String formTemplateNameInLCRepository = (String)httpSession.getAttribute("formTemplateNameInLCRepository");
            String dscCredentialName =(String)httpSession.getAttribute("DSC_CREDENTIAL_USERNAME");
            String dscCredentialPass =(String) httpSession.getAttribute("DSC_CREDENTIAL_PASSWORD");
            httpSession.removeAttribute("formTemplateURL");
            httpSession.removeAttribute("dataStoreName");
            httpSession.removeAttribute("formTemplateNameInLCRepository");
            httpSession.removeAttribute("DSC_CREDENTIAL_USERNAME");
            httpSession.removeAttribute("DSC_CREDENTIAL_PASSWORD");
            
            SamplesConnectorforIBMCM_Processes_ArchiveSubmittedDataWithNRIBMCMServiceLocator archSubmitDataServLoc = new SamplesConnectorforIBMCM_Processes_ArchiveSubmittedDataWithNRIBMCMServiceLocator();
            SamplesConnectorforIBMCM_Processes_ArchiveSubmittedDataWithNRIBMCM submitClient = archSubmitDataServLoc.getArchiveSubmittedDataWithNRIBMCM();
            ((javax.xml.rpc.Stub)submitClient)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, dscCredentialName);
            ((javax.xml.rpc.Stub)submitClient)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, dscCredentialPass);
            BLOB submittedFormData = new BLOB();
            System.out.println("Setting the binary data");
            submittedFormData.setBinaryData(formDatabuf);
            submittedFormData.setContentType(SubmitFormServlet.FORM_DATA_MIME_TYPE);
            
            System.out.println("Arguments used at service invocation: " );
            System.out.println("DataStore Name: " + dataStoreName + "\nEnvironment Buffer: " + environmentVariables + "\nDSC UserName: " 
            + dscCredentialName + "\nItem Type: " + SubmitFormServlet.FORM_DATA_ITEM_TYPE + "\nMime Type: " + SubmitFormServlet.FORM_DATA_MIME_TYPE + 
            "\nxdp Data Title: " + formTemplateNameInLCRepository + "\nxdp Path In Repository: " + formTemplateURL);

            // invoke service: ArchiveSubmittedDataWithNR-IBMCM 
            submitClient.invoke(dataStoreName, environmentVariables, dscCredentialPass, dscCredentialName, SubmitFormServlet.ALO_OBJECT_ITEM_TYPE, 
            SubmitFormServlet.FORM_DATA_ITEM_TYPE, SubmitFormServlet.FORM_DATA_MIME_TYPE, submittedFormData, 
            formTemplateNameInLCRepository, formTemplateURL);

            PrintWriter pw =response.getWriter();
            pw.println("<html>");
            pw.println("<head></head>");
            pw.println("<body>");
            pw.println("<script>window.close();</script>");
            pw.println("</body>");
            pw.println("</html>");
            System.out.println("Exit SubmitFormServlet");
            pw.flush();
        }catch(Throwable fault)
        {
            System.out.println("Exception caught");
            fault.printStackTrace();
            throw new ServletException(fault);
        }
    }

    public static String getCurrentDateAsString()
    {
        Calendar cal = Calendar.getInstance();

        String str = ""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+
        cal.get(Calendar.DAY_OF_MONTH)+"_"+cal.get(Calendar.HOUR_OF_DAY)+"_"+cal.get(Calendar.MINUTE)+
        "_"+cal.get(Calendar.SECOND);
        return str;
    }
}