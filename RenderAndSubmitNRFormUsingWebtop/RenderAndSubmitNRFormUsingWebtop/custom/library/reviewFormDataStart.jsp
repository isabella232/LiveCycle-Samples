<%@page contentType="text/html"%>

<%@ page errorPage="/wdk/errorhandler.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/dmform_1_0.tld" prefix="dmf" %>
<%@ taglib uri="/WEB-INF/tlds/dmformext_1_0.tld" prefix="dmfx" %>
<%@ page import="java.util.Random" %>
<html>
<head>
    <dmf:webform />
    <script language="JavaScript1.2">
        
<%  
	Random r = new Random();
    int i = r.nextInt();
    if (i < 0) i = i * -1;
    String randomWindowName = "a" + String.valueOf(i);
	String remoteUrl   = (String) request.getAttribute("remoteUrl");
%>    
   

        function openURLInNewWindow()
		{
			window.open( '<%=remoteUrl%>', '<%=randomWindowName%>');
			postServerEvent(null, null, null, "onComponentReturn");
			return true;
        }

    </script>
    
</head>


<body onload="javascript: openURLInNewWindow()">
<dmf:form>
<dmf:label label='Please wait while the Form Loads.'/>
</dmf:form>
</body>
</html>
