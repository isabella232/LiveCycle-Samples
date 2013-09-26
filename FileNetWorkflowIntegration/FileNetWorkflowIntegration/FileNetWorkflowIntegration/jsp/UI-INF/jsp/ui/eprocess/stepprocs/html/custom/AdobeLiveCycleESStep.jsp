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

<%@ include file="/WEB-INF/jsp/WcmHeader.jsp" %>

<html>
<head>
    <%
        WcmString topic = 
           new WcmString("server.properties_AdobeLiveCycleESStep_jsp.topic", "Adobe LiveCycle ES Step Processor");
        WcmWorkplaceUi.renderHeaders(request, out, true, topic);
    %>
</head>
<body class="proBody">
    <% WcmUi.render(request, "adobeLiveCycleESStepModule", out); %>
</body>
</html>