<%--
 * ADOBE SYSTEMS INCORPORATED
 * Copyright 2007 Adobe Systems Incorporated
 * All Rights Reserved
 * 
 * NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
 * terms of the Adobe license agreement accompanying it.  If you have received this file from a
 * source other than Adobe, then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
--%>

<%@ page errorPage="/WcmError.jsp" autoFlush="false" %>

<%-- UI Beans --%>
<jsp:useBean
    id="adobeLiveCycleESStepModule"
    class="com.adobe.livecycle.samples.connectorforibmfilenet.filenetworkflowintegration.stepprocessor.ui.AdobeLiveCycleESStepModule"
    scope="request">
    <jsp:setProperty name="adobeLiveCycleESStepModule" property="name" value="adobeLiveCycleESStepModule" />
</jsp:useBean>

<%-- Data Provider Bean --%>
<jsp:useBean
    id="eProcessDataProvider"
    class="com.filenet.wcm.toolkit.server.dp.WcmEProcessDataProvider"
    scope="request">
    <jsp:setProperty name="eProcessDataProvider" property="name" value="eProcessDataProvider" />
</jsp:useBean>

<%-- WcmController CONTROLLER --%>
<jsp:useBean
    id="controller"
    class="com.filenet.wcm.apps.server.controller.WcmWorkplaceController"
    scope="request">
</jsp:useBean>

<%
    adobeLiveCycleESStepModule.addDataProvider(eProcessDataProvider);
    
    controller.configurePage(application, request, controller.isNonReturnableBookmark(request));
    controller.registerModule(eProcessDataProvider);
    controller.registerModule(adobeLiveCycleESStepModule);
    controller.handleEvent(application, request, response, true);
%>