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

public class Constants
{
    // Relative Path names of the servlets used
    public static final String RELATIVE_PATH_ADOBE_LAUNCH_WORKFLOW_RENDER_SERVLET = "/AdobeLaunchWorkflowRenderServlet";
    public static final String RELATIVE_PATH_ADOBE_LAUNCH_WORKFLOW_SUBMIT_SERVLET = "/AdobeLaunchWorkflowSubmitServlet";
    public static final String RELATIVE_PATH_ADOBE_LC_ES_STEP_SUBMIT_SERVLET = "/AdobeLiveCycleESStepSubmitServlet";
    public static final String RELATIVE_PATH_ADOBE_LC_ES_STEP_RENDER_SERVLET = "/AdobeLiveCycleESStepRenderServlet";

    // Services used during Launch Workflow
    public static final String ARCHIVE_SUBMITTED_DATA_FILENET = "Samples%20-%20ConnectorforIBMFileNet/Processes/ArchiveSubmittedDataWithNR-FileNet";
    public static final String RENDER_FORMS_FROM_NR_FILENET = "Samples%20-%20ConnectorforIBMFileNet/Processes/RenderFormsFromNR-FileNet";

    // Session parameters
    public static final String SESSION_USERID = "USERID";
    public static final String SESSION_PASSWORD = "PASSWORD";
    public static final String SESSION_OBJECT_STORE_NAME = "objectStoreName";
    public static final String SESSION_FOLDER_PATH = "folderPath";
    public static final String SESSION_XDP_FILE_NAME = "xdpName";
    public static final String SESSION_ROUTER_URL = "ROUTER_URL";
    public static final String SESSION_QUEUE_NAME = "QUEUE_NAME";
    public static final String SESSION_WOB_NUM = "WOB_NUM";

    // Inputs used for invocation of Render Service
    public static final String STEP_PARAMETER_RENDER_SVC_NAME = "renderServiceName";
    public static final String SUBMIT_URL = "submitURL";

    // Parameters implicitly passed to Render and Submit Service(s)
    public static final String USERID = "userID";
    public static final String PASSWORD = "password";
    public static final String QUEUE_NAME = "queueName";
    public static final String WORK_OBJECT_NUMBER = "workObjectNumber";

    // Inputs used for invocation of Submit Service
    public static final String STEP_PARAMETER_SUBMIT_SVC_NAME = "submitServiceName";

    // Parameters implicitly passed to Submit Service
    public static final String ENVIRONMENT_VARIABLES = "environmentVariables";
}