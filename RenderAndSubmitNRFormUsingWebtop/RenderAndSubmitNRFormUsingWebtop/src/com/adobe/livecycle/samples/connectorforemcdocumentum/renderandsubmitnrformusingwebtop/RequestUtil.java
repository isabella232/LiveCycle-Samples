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

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil 
{
	  
	  static final int BLOCK_SIZE = 512;
	  /**
	   * Reads the raw request input buffer.
	   * @param request
	   * @return String
	   * @throws IOException
	   * @throws ServletException
	   */
	  public static byte[] getRequestBuffer(HttpServletRequest request)
	    throws IOException, ServletException
	  {
	    byte[] cRequestBuffer = new byte[0];

	    // get the RequestBuffer
	    ServletInputStream oInput = request.getInputStream();
	    long nContentLength = request.getContentLength();
	    
	    if (nContentLength <= 0)
	      return cRequestBuffer;

	    // read the content in 512 bytes chunks 
	    // a single read does not get all the characters
	    byte[] cContent = new byte[(int)nContentLength];
	    
	    int nRead = 0;
	    int nToRead = (int)nContentLength;
	    int nBlkSize = BLOCK_SIZE;
	    byte[] cTemp = new byte[BLOCK_SIZE];
	    
	    do
	    {
	      int n = 0;
	      if (nToRead - nRead < BLOCK_SIZE)
	        nBlkSize = nToRead - nRead;

	      n = oInput.read(cTemp, 0, nBlkSize);
	      if (n < 0 && nBlkSize > 0)
	        throw new IOException ("read EOF on inputStream with " + nBlkSize + " bytes pending");
	        
	      System.arraycopy (cTemp, 0, cContent, nRead, n);
	      nRead += n;
	    }
	    while (nRead < nToRead);
	    
	    return cContent;
	  }
	  
	  /**
	   * Constructs a string buffer containing environment variables.
	   * 
	   * @param request
	   */
	  public static String getEnvironBuffer(HttpServletRequest request)
	  {
	    //
	    // add server variables
	    //
	    String sEnvironBuffer;
	    sEnvironBuffer = addVariable(request, "Referer", "HTTP_REFERER");
	    sEnvironBuffer += addVariable(request, "Connection", "HTTP_CONNECTION");
	    sEnvironBuffer += addVariable(request, "Accept", "HTTP_ACCEPT");
	    sEnvironBuffer += addVariable(request, "Accept-Language", "HTTP_ACCEPT_LANGUAGE");
	    sEnvironBuffer += addVariable(request, "Accept-Encoding", "HTTP_ACCEPT_ENCODING");
	    sEnvironBuffer += addVariable(request, "Host", "HTTP_HOST");
	    sEnvironBuffer += addVariable(request, "User-Agent", "HTTP_USER_AGENT");
	    sEnvironBuffer += addVariable(request, "Content-Type", "CONTENT_TYPE");
	    sEnvironBuffer += addVariable(request, "Content-Length", "CONTENT_LENGTH");
	    sEnvironBuffer += addVariable(request, "FS-Request", "HTTP_FS_REQUEST");
	    sEnvironBuffer += addVariable(request, "ServerName", "SERVER_NAME");

	    return sEnvironBuffer;
	  }

	  /**
	   * Reads the server variable and returns it as a name=value string.
	   * 
	   * @param request
	   * @param sName
	   * @param sVariableName
	   * @exception java.lang.IOException, java.lang.ServletException
	   */
	  public static String addVariable(HttpServletRequest request,String sName,String sVariableName)
	  {
	    String sValue=null;
	    String sReturn = "";
	    if (sVariableName == "SERVER_NAME")
	    {
	    	sValue = request.getServerName();
	    }
	    else
	    {
	    	sValue = request.getHeader(sName);
	    }

	    if (sValue != null)
	    {
	    	sReturn += "&" + sVariableName + "=" + sValue;
	    }
	    return sReturn;
	  }
	  
	  public static String getBaseUrl(HttpServletRequest request)
	  {
	    StringBuffer baseUrl = new StringBuffer(request.getScheme());
	    baseUrl.append("://");
	    baseUrl.append(request.getServerName());
	    baseUrl.append(":");
	    baseUrl.append(request.getServerPort());
	    baseUrl.append(request.getContextPath());
	    return baseUrl.toString();
	  }

}
