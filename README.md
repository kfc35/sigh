kfc35m ss2249, hhc39

To run:
  Deploy the .war file into the tomcat7 webapps directory

Our code is separated into two components:
The java code:
	Terminator - a thread that removes expired session from the table
	BasicSessionServlet - Is called in both GET and POST instances. GET is for first time access and refresh, which we determined were very similar. POST is for Log out and Replace. There's a concurrent hash map for the sessions, and the sessionId is UUID.
	CS5300PROJ1SESSION - the session object. In addition to its inherent values, it also has a string sessionId, a string message, and a long end (expiration time). The version number is the parent's version.

The first.jsp:
	The html is rendered per response

Concurrency:
	The session table is a concurrent hash map, and all instances to it are "synchronized"

Cheers, 
Kevin, Horace, and Sweet
