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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class LCUtil 
{
	  public static Properties getServiceClientProps(){
		    
		    ResourceBundle connectionBundle = ResourceBundle.getBundle
		    ("com/adobe/livecycle/samples/connectorforemcdocumentum/renderandsubmitnrformusingwebtop/resources/AdobeLCES");
		    
		     Enumeration names = connectionBundle.getKeys();        
		     Properties props = new Properties();
		    while (names.hasMoreElements()) 
		    {
		        String name = names.nextElement().toString();
		        String value = (String) connectionBundle.getObject(name);  
		        props.put(name,value);
		    }
	    return props;	    
	  }
	  
	  public static boolean isBlankString(String s)
	  {
	    return (s== null || "".equals(s));
	  }

	  public static String getCurrentDateAsString()
	  {
	    Calendar cal = Calendar.getInstance();
	    
	    String str = ""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+
	    cal.get(Calendar.DAY_OF_MONTH)+"_"+cal.get(Calendar.HOUR_OF_DAY)+"_"+cal.get(Calendar.MINUTE)+
	    "_"+cal.get(Calendar.SECOND);
	    return str;
	  }
	  
	  public static final String  getStackTrace(Throwable t) 
	  {
	      if(t!=null)
	      {
	        StringWriter stringWritter = new StringWriter();
	        PrintWriter printWritter = new PrintWriter(stringWritter, true);
	        t.printStackTrace(printWritter);
	        printWritter.flush();
	        stringWritter.flush(); 
	       return stringWritter.toString();
	      }
	      return "";
	    }
	  
	  
	  /*
	   * For EJB Invocation of LC Service
	   */
/*	public static Object invokeLCService(Map inputParams, Properties connectionProps,String serviceName,String methodName,String outputParamName) throws DSCException 
	{
		ServiceClientFactory serviceFactory = ServiceClientFactory.createInstance(connectionProps);
		System.out.println("Service Client Factory constructed");
		ServiceClient serviceClient = serviceFactory.getServiceClient();
		System.out.println("Service Client constructed");		  
		
		System.out.println("Service Input Parameters : ");
		System.out.println(inputParams);
		
		InvocationRequest invocationRequest= serviceFactory.createInvocationRequest
											(serviceName,methodName,inputParams,true);
		
		System.out.println("Invocation Request constructed");
		InvocationResponse invocationResponse=serviceClient.invoke(invocationRequest);
		System.out.println("Invocation Response recieved");
		
		Object output = invocationResponse.getOutputParameter(outputParamName);
		return output;
	}
*/		  
		  
}
