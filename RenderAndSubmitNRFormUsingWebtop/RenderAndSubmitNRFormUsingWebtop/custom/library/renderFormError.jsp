<%@ page errorPage="/wdk/errorhandler.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/dmform_1_0.tld" prefix="dmf" %>
<%@ taglib uri="/WEB-INF/tlds/dmformext_1_0.tld" prefix="dmfx" %>
<%@ page import="com.documentum.web.form.Form" %> 

<html>

<head>
	<dmf:webform />
	<%  
		String errorTrace   = (String) request.getAttribute("errorTrace");
	%>    	
</head>

<body>
	<dmf:form>
		<font color="darkred"><b><%=errorTrace%></b></font>		
	</dmf:form>
</body>

</html>
