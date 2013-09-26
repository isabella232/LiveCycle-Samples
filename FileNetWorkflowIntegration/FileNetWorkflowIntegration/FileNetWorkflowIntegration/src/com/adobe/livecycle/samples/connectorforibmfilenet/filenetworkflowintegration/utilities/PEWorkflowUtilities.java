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
package com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.utilities;

import java.util.Arrays;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWTransferResult;
import filenet.vw.api.VWWorkflowDefinition;


public class PEWorkflowUtilities
{
    /** Transfer Workflow Definition */
    private static String transferWorkflow(VWSession session, VWWorkflowDefinition wfDefinition)
    throws VWException
    {     
        String _transferVersion = null;
        
        VWTransferResult _transferResult = session.transfer(wfDefinition, "sampleWorkflowID", false, true);        
        if (_transferResult.success())
        {
            System.out.println("Transfer of workflow: SUCCESSFUL.");
            _transferVersion = _transferResult.getVersion();
        }
        else
        {
            System.out.println("Transfer of workflow: FAILED.");
            
            // Display the transfer errors
            String[] _errors = _transferResult.getErrors();
            if (_errors != null)
            {
                System.out.println("Following transfer errors occured:");
                System.out.println(Arrays.asList(_errors));                
            }
            else
            {
                System.out.println("Error messages are not available.");
            }
        }
        
        return _transferVersion;
    }
    
    /** Launch Workflow with the given transfer version and values for the parameters */ 
    private static void launchWorkflow(VWSession session, String wfTransferVersion, java.util.HashMap launchStepParameters)
    throws VWException    
    {
       VWStepElement _launchStepElement = null;
       String[] _stepParameterNames = null;

       // Launch the workflow based on the version string passed back from transfer.
       _launchStepElement = session.createWorkflow(wfTransferVersion);
       _launchStepElement.setComment("Initiating Workflow Process");
       PEStepElementUtilities.setStepParameterValues(_launchStepElement, launchStepParameters);
       
       // Display properties of the workflow / launch step
       System.out.println("Launch Step information:");
       System.out.println("\tWorkflow Name:"    + _launchStepElement.getWorkflowName());
       System.out.println("\tSubject:"          + _launchStepElement.getSubject());
       System.out.println("\tComment:"          + _launchStepElement.getComment());
       System.out.println("\tStep Description:" + _launchStepElement.getStepDescription());       
       System.out.println("Parameters:");
       _stepParameterNames = _launchStepElement.getParameterNames();
       if (_stepParameterNames == null)
       {
           System.out.println("\t\t No Parameters!");
       }
       else
       {
           // display the parameter names and their values
           for (int i = 0; i < _stepParameterNames.length; i++)
           {
               if (_stepParameterNames[i] != null)
               {
                   Object _parameterValue = _launchStepElement.getParameterValue(_stepParameterNames[i]);
                   System.out.println("\t" + _stepParameterNames[i] + "=" + _parameterValue);
               }
           }
       }

        // Continue execution of this workflow.
       _launchStepElement.doDispatch();
    }

    /** 
     * Prepares and launches workflow
     * 
     * @param userName              User Name for the process engine session
     * @param password              Password for the process engine session
     * @param routerURL             Router URL for the Process Engine
     * @param wfDefFile             Path of the workflow definition
     * @param launchStepParameters  Name/Value map for the launch step parameters
     *   
     * @throws VWException
     */
    public static void prepareAndLaunchWorkflow(String userName, String password, String routerURL, 
            String wfDefFile, java.util.HashMap launchStepParameters)
    throws VWException    
    {
        VWSession _session = new VWSession(userName, password, routerURL);
        VWWorkflowDefinition _workflowDefinition = VWWorkflowDefinition.readFromFile(wfDefFile);
        String _wfVersion = transferWorkflow(_session, _workflowDefinition);
        launchWorkflow(_session, _wfVersion, launchStepParameters);
        _session.logoff();
    }    
}