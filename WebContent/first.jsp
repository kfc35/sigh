<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>kfc35 - ss2249 - hhc39 - CS5300 - Proj 1a</title>
</head>
<body>
<h1><%=getServletContext().getAttribute("message")%></h1>
<!-- The BasicSessionServlet is mapped to "/", so that's why action is "" -->
<form method="get" action=""><input type="submit" name="Replace" value="Replace"/>
	<input type="text" name="newMessage" size="40" maxLength="100" /></form>
	<!-- TODO look into maxLength for the message -->
<form method="get" action=""><input type="submit" name="Refresh" value="Refresh"/></form>
<form method="get" action=""><input type="submit" name="Log out" value="Log out"/></form>
<p>Session on <%=getServletContext().getAttribute("address")%></p>
<p>Expires <%=getServletContext().getAttribute("expires")%></p>
</body>
</html>