package com.adobe.livecycle.samples.ibmcm;

import javax.servlet.*;
import javax.servlet.http.*;

import com.adobe.idp.services.BLOB;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_RenderFormsFromNRIBMCM;
import com.adobe.idp.services.SamplesConnectorforIBMCM_Processes_RenderFormsFromNRIBMCMServiceLocator;
import com.adobe.livecycle.samples.ibmcm.eClient.util.RequestUtil;
import com.ibm.mm.beans.CMBConnection;
import com.ibm.mm.beans.CMBDataManagement;
import com.ibm.mm.beans.CMBItem;
import com.ibm.mm.beans.CMBObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdobeFormsServlet extends HttpServlet
{
    private static final long serialVersionUID = 3564339674563786794L;
    static final String PATH_SEPARATOR = "/";
    
    /**
     * Initialize method to AdobeDPConfirmPrompt layout module..
     * @throws ServletException
     */
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
      * This method handles the request from the client to render a form 
      * which is created through AdobeDocumentPolicy.
      *
      * @param request
      * @param response
      * @throws ServletException
      * @throws IOException
      */
     
     public void doProcess(HttpServletRequest request, HttpServletResponse response)
     throws ServletException,IOException {
         
         CMBConnection _cmbConnection = null;
         System.out.println("Enter AdobeFormsServlet");
         HttpSession session =null;

         String xdpPathInNativeRepository = null;
         String formTemplateNameInLCRepository = null;
         String xdpFolderPathInLCRepository = null;

         try
         {

             // Get PID of ALO.
             String pidOfXdpAlo = request.getParameter("document");

             // Session related work
             session = request.getSession(false);
             _cmbConnection = (CMBConnection) session.getAttribute("connectionBean");

             // Obtain credentials and other useful information. 
             String dataStoreName = _cmbConnection.getDatastore().datastoreName();
             String dscCredentialName = _cmbConnection.getUserid();
             String dscCredentialPass = _cmbConnection.getPassword();

             // Obtain the title attribute of XDP.
             CMBItem _cmbItem = new CMBItem();
             _cmbItem.setConnection(_cmbConnection);
             _cmbItem.setPidString(pidOfXdpAlo);
             

             CMBDataManagement _cmbDataMgmt = _cmbConnection.getDataManagement();
             _cmbDataMgmt.setDataObject(_cmbItem);
             _cmbDataMgmt.retrieveItem();

             CMBObject[] _cmbObjects = _cmbDataMgmt.getContent();
             List _cmbObjectsByteContentList = new ArrayList();
             
             byte[] byteArray = new byte[1024*1024];
             
             for (int i = 0; i < _cmbObjects.length; i++)
             {
                 CMBObject cmbObject = _cmbObjects[i];
                 InputStream cmbObjectStream = cmbObject.getDataStream();

                 while(true)
                 {
                     int len = cmbObjectStream.read(byteArray);
                     if(len==-1)
                         break;
                     for (int j = 0; j < len; j++)
                     {
                         _cmbObjectsByteContentList.add(Byte.toString(byteArray[j]));
                     }
                }
             }

             byte[] cmbObjectsByteDataArray = new byte[_cmbObjectsByteContentList.size()];
             for (int i = 0; i < cmbObjectsByteDataArray.length; i++)
             {
                 cmbObjectsByteDataArray[i] = Byte.parseByte((String)_cmbObjectsByteContentList.get(i));
             }
             xdpPathInNativeRepository = new String(cmbObjectsByteDataArray);
             
             if (xdpPathInNativeRepository != null)
             {
                 formTemplateNameInLCRepository = getXdpNameFromURL(xdpPathInNativeRepository);
                 xdpFolderPathInLCRepository = getXdpFolderPathFromURL(xdpPathInNativeRepository);
             }
             
             // Set service invocation credential information 
             SamplesConnectorforIBMCM_Processes_RenderFormsFromNRIBMCMServiceLocator renderServiceLocator = new SamplesConnectorforIBMCM_Processes_RenderFormsFromNRIBMCMServiceLocator();
             SamplesConnectorforIBMCM_Processes_RenderFormsFromNRIBMCM renderClient = renderServiceLocator.getRenderFormsFromNRIBMCM();
             ((javax.xml.rpc.Stub) renderClient)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, dscCredentialName);
             ((javax.xml.rpc.Stub) renderClient)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, dscCredentialPass);

             // Set the URL to direct form when submit button is clicked.
             String baseUrl = RequestUtil.getBaseUrl(request);
             String submitUrl = baseUrl + "/SubmitFormServlet";
             
             System.out.println("Arguments used at service invocation: " );
             System.out.println("Base URL: " + baseUrl + "\nDataStore Name: " + dataStoreName + "\nDSC User Name: " 
             + dscCredentialName + "\nPID of XDP Form: " + pidOfXdpAlo + "\nSubmit URL: " + submitUrl);

             // invoke service: RenderFormsFromNR-IBMCM
             BLOB renderedBlob = renderClient.invoke(submitUrl, null, xdpFolderPathInLCRepository, xdpPathInNativeRepository);
             
             System.out.println("pdf blob returned" + renderedBlob.toString());

             String remoteUrl = renderedBlob.getRemoteURL();
             System.out.println("BLOB: Remote URL:- " + remoteUrl);

             HttpSession httpSession = request.getSession();
             httpSession.setAttribute("formTemplateURL", xdpPathInNativeRepository);
             httpSession.setAttribute("dataStoreName", dataStoreName);
             httpSession.setAttribute("formTemplateNameInLCRepository", formTemplateNameInLCRepository);
             httpSession.setAttribute("DSC_CREDENTIAL_USERNAME", dscCredentialName);
             httpSession.setAttribute("DSC_CREDENTIAL_PASSWORD", dscCredentialPass);

             System.out.println("Redirect to remoteURL");
             response.sendRedirect(remoteUrl);
             
         }
         catch (Throwable fault)
         {
             System.out.println("Exception caught");
             fault.printStackTrace();
             throw new ServletException(fault);
         }
         
         System.out.println("Exit AdobeFormsServlet");
     }


     private String getXdpNameFromURL(String formTemplateURL)
     {
         int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(AdobeFormsServlet.PATH_SEPARATOR);
         String formTemplateName = formTemplateURL.substring(lastIndexOfPathSeperator + 1, formTemplateURL
         .length());
         return formTemplateName;
     }

     private String getXdpFolderPathFromURL(String formTemplateURL)
     {
         int lastIndexOfPathSeperator = formTemplateURL.lastIndexOf(AdobeFormsServlet.PATH_SEPARATOR);
         String formTemplateFolderPath = formTemplateURL.substring(0, lastIndexOfPathSeperator);
         return formTemplateFolderPath;
     }


}




