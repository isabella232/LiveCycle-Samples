/**************************************************************************
*
* ADOBE CONFIDENTIAL
* ___________________
*
*  Copyright 2002-2005 Adobe Systems Incorporated
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property 
* of Adobe Systems Incorporated and its suppliers, if any.  The 
* intellectual and technical concepts contained herein are proprietary to 
* Adobe Systems Incorporated and its suppliers and may be covered by U.S. 
* and Foreign Patents, patents in process, and are protected by trade 
* secret or copyright law.  Dissemination of this information or 
* reproduction of this material is strictly forbidden unless prior written 
* permission is obtained from Adobe Systems Incorporated.
**************************************************************************/

package com.adobe.livecycle.samples.ibmcm.eClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.idm.NLS.IDMResourceManager;
import com.ibm.idm.servlets.IDMErrorBean;
import com.ibm.idm.servlets.IDMServletConstants;
import com.ibm.idm.util.IDMServletUtils;
import com.ibm.mm.beans.CMBConnection;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKLobICM;
import com.ibm.mm.sdk.common.DKParts;
import com.ibm.mm.sdk.common.dkDatastore;
import com.ibm.mm.sdk.common.dkIterator;
import com.ibm.mm.sdk.server.DKDatastoreICM;


public final class DecideRoute extends HttpServlet
   implements IDMServletConstants
{
    private static final long serialVersionUID = 3564339678657455475L;
    
    private static Logger logger = Logger.getLogger(DecideRoute.class.getName());

    public static final int ERROR_CODE_ECLIENT = 90000;
        
    public static final String ALO_MIME_TYPE = "webapps-x-adobe-assetlink";
    public static final String FORM_DATA_MIME_TYPE = "webapps-x-adobe-submitteddata";
   
    private static String m_contextURL = null;
    private CMBConnection m_CMBConnection = null;
    
    
   public void doGet(HttpServletRequest request, HttpServletResponse response)
       throws IOException, ServletException
   {
       logger.log(Level.INFO ,"DecideRoute.doGet() -> Entry");

/********************************************************************************
* This section of code gets the connection information out of the session/context.
* It is currently coded for the IBM eClient. If this servlet is being called from
* another client, it should be changed to get the connection information stored
* by that client using that client's beans or APIs.
********************************************************************************/

       try
       {
            if (!IDMServletUtils.isConnected(getServletContext(), request, response, "SessionErrorPage.jsp", this))
            {
                return;
            }
        
            HttpSession session = request.getSession(false);
       
            // Get the context URL from the request.
            m_contextURL = getContextURL(request);//http://ibmcm/eClient82

            // Get CMB Connection Bean from the session's connection attribute. This bean contains 
            // the information about the user logged into the IBM Content Manager eClient.
            m_CMBConnection = (CMBConnection)session.getAttribute("connection");
           
            // Set the connectionBean as attribute in the session for later use by Adobe Servlets.
            session.setAttribute("connectionBean", m_CMBConnection);
            
            logger.log(Level.INFO ,"User Id from the Connection Bean: " + m_CMBConnection.getUserid());
            logger.log(Level.INFO ,"Credentials from the Connection Bean: " + m_CMBConnection.getCredential());
            
            // Get the pid of the document user clicked in eClient. PID is the unique identifier
            // for all objects stored in the IBM Content Manager.
            String pidString = IDMResourceManager.getParameter(request, "document");
            
/********************************************************************************
* End of client specific code
********************************************************************************/
           try
           {
                // Get the connected datastore. 
                dkDatastore dsDK = m_CMBConnection.getDatastore();

                if (dsDK == null)
                {
                    // If null, error since no datastore means there is no way to reach the document.
                    throw new IllegalStateException("Datastore is null");
                }                   
/*************************************************************************/
                // Check if the request is from ICM. We only handle those requests which are from ICM
                // Alternatively requests can also be from "FED" which is not supported by this sample. 
                String datastoretype = dsDK.datastoreType();

               if (!datastoretype.equals("ICM"))
               {
                   // if not Content Manager V8.x ("ICM") forward to client application
                   logger.log(Level.INFO , "Forwarding to Client applications - not ICM");
                   forwardToClient(request, response);
               }
/*************************************************************************/
               else
               {
                   String mimeType = getMimeType(pidString);
                   if((mimeType!=null)&& (mimeType.equals(ALO_MIME_TYPE) || mimeType.equals(FORM_DATA_MIME_TYPE)))
                   {
                       // Forward the request to Adobe Servlet only when the document represented by pidString is an 
                       // Asset Link Object/Form Data XML 
                       forwardToAdobe(request, response, mimeType);
                   }
                   else
                   {
                       // Forward request to default IBM's eClient viewer.
                       forwardToClient(request, response);
                   }
               }
           }
           catch(DKException dkexcept)
           {
            // Add code to return error to error page in correct locale
                sendError(request, response, dkexcept, "set error - DKException " + dkexcept.getMessage());
                return;
           }
           catch(Exception exception)
           {
            // Add code to return error to error page in correct locale
                sendError(request, response, exception, "set error - Exception " + exception);
               return;
           }

       }
       catch(Exception exception)
       {
        // Add code to return error to error page in correct locale
            sendError(request, response, exception, "set error - Exception " + exception);
           return;
       }
       catch(Throwable throwable)
       {
           // Add code to return error to error page in correct locale
            sendError(request, response, throwable, "set error - Throwable " + throwable);
           return;
       }
       logger.log(Level.INFO, "End decide routing");
   }

   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
       throws IOException, ServletException
   {
       doGet(request, response);
   }

   /*
    * Forwards the request to IBM's eClient viewer.
    */
   private void forwardToClient(HttpServletRequest request, HttpServletResponse response)
           throws IOException
   {
       logger.log(Level.INFO, "DecideRoute.forwardToClient() -> Entry");

        // The client servlet to forward this request to should have been stored
        // in the session by the calling JSP.
       String toServlet = (String) request.getSession().getAttribute("toServlet");
       if (toServlet == null)
       {
           // If toServlet not set - default to IBM eClient Viewer servlet
            logger.log(Level.INFO, "toServlet is null");
            toServlet = "IDMDocViewer";
       }
       try
       {    
        logger.log(Level.INFO, "forward to " + toServlet);
        RequestDispatcher requestdispatcher = getServletContext().getRequestDispatcher(toServlet);
        requestdispatcher.forward(request, response);
       }
       catch(ServletException servletException)
       {
            sendError(request, response, servletException, "Failed to forward session to "  + toServlet);  
            return;
       } 
   }
   
   /*
    * Forwards the request to Adobe Servlet.
    */
   private void forwardToAdobe(HttpServletRequest request, HttpServletResponse response, String mimeType)
   throws IOException
{
       logger.log(Level.INFO, "DecideRoute.forwardToAdobe() -> Entry");

       try
       {
           String afsURL = null;

           if (mimeType != null)
           {
               if (mimeType.equals(ALO_MIME_TYPE))
                   afsURL = "/AdobeFormsServlet";
               else if (mimeType.equals(FORM_DATA_MIME_TYPE))
                   afsURL = "/ReviewFormDataHandler";
           }

           RequestDispatcher requestdispatcher = request.getRequestDispatcher(afsURL);
           requestdispatcher.forward(request, response);

       }
       catch (ServletException servletException)
       {
           sendError(request, response, servletException);
       }
       logger.log(Level.INFO, "DecideRoute.forwardToAdobe() -> Exit");
       return;
}

   private void sendError(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
   {
    try
    {
            sendError(request, response, throwable, throwable.getMessage());
    }
    catch (IOException ioException) 
    { 
        logStackTrace(ioException);
    }
   }

   private void sendError(HttpServletRequest request, HttpServletResponse response, Throwable e, String errorDesc)
    throws IOException
   {
        try
       {    
            logger.log(Level.INFO, errorDesc);
            logStackTrace(e);
            
            IDMErrorBean error = new IDMErrorBean();
            error.setMessage(errorDesc);
            error.setLoginPage("IDMInit");
            request.getSession().setAttribute(IDMServletConstants.BEAN_ErrorBean, error);
                    
        logger.log(Level.INFO, "DecideRoute redirecting to ErrorPage.jsp: " + e.getMessage());
        response.sendRedirect("ErrorPage.jsp");
        
       }
       catch (IOException redirectException)
       {
        String errorMsg = "Failed to redirect sesson to Adobe\r" + redirectException;
        logger.info(errorMsg);

        logger.log(Level.INFO, "DecideRoute Sending Error: " + redirectException.getMessage());
           response.sendError(400, errorMsg);
       }
       catch (Exception formInfoException)
       {
        logger.log(Level.INFO, formInfoException.getMessage());

        logger.log(Level.INFO, "DecideRoute Sending Form Info Error via Integration Server");
            response.sendRedirect(m_contextURL + "/failure.jsp?code=" + ERROR_CODE_ECLIENT);
       }
   }    
   
    private String getContextURL(HttpServletRequest req)
    {
        StringBuffer url = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        String urlPath = req.getContextPath();//getRequestURI();
        url.append(scheme);
        url.append("://");
        url.append(req.getServerName());
        if(scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443)
        {
            url.append(':');
            url.append(req.getServerPort());
        }
        url.append(urlPath);
        
        return url.toString();
    }
    
    
    private static void logStackTrace(Throwable t)
    {
        if (t == null) return;
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        logger.info(sw.toString());
    }
    
    /*
     *  Gets the mime Type corresponding to the document identified by the pidString. 
     */
    private String getMimeType(String pidString) throws Exception
    {
        logger.log(Level.INFO ,"DecideRoute.getMimeType() -> Entry");
        logger.log(Level.INFO ,"Pid: " + pidString);
        
        DKDDO ddo = ((DKDatastoreICM)m_CMBConnection.getDatastore()).createDDO(pidString);
        ddo.retrieve(DKConstant.DK_CM_CONTENT_YES);
        
        String mimeType = null;
        
        short dataid = ddo.dataId(DKConstant.DK_CM_NAMESPACE_ATTR,
         DKConstant.DK_CM_DKPARTS); // returns 0, if the namespace or data-item
                                                                 // name is not found
         if (dataid > 0) {
             DKParts dkParts = (DKParts) ddo.getData(dataid); // obtain
             if (dkParts != null && dkParts.cardinality() > 0) {
                 dkIterator iter = dkParts.createIterator(); // Create an iterator to DkParts
                     DKLobICM part = (DKLobICM) iter.next();
                     mimeType = part.getMimeType();
                 }
             }
         logger.log(Level.INFO ,"mimeType: " + mimeType);
         logger.log(Level.INFO ,"DecideRoute.getMimeType() -> Exit");
         return mimeType;
    }
    
}
