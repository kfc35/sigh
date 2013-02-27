package sessions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BasicSessionServlet
 */
@WebServlet("/BasicSessionServlet")
public class BasicSessionServlet extends HttpServlet {
	public static boolean DEBUG = true;
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "Hello, User!";
	private ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable = 
			new ConcurrentHashMap<String, CS5300PROJ1SESSION>();
	
	public static int servletNum = 0;
	private static final long EXPIRY_TIME_FROM_CURRENT = 1000 * 120; //2 minutes
	private Thread terminator = new Thread(new Terminator(sessionDataTable));
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BasicSessionServlet() {
        super();
        // TODO Auto-generated constructor stub
        terminator.start();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CS5300PROJ1SESSION session = null;
		/*Process session information if applicable*/
		Cookie[] cookies = request.getCookies();
		Cookie cookieToSend = null;
		String message = null;
		long end = 0;
		
		boolean foundCookie = false;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(CS5300PROJ1SESSION.COOKIE_NAME)) {
					String sessionId = c.getValue();
					session = sessionDataTable.get(sessionId);
					if (session == null) { 
						/*this can happen if you stop the servlet, clearing the
						  concurrent hashmap, and then run it again -> Eclipse still has
						  the cookie! Make a new cookie now!*/
						break;
					}
					
					message = session.getMessage();
					end = session.getEnd();
					foundCookie = true;
					cookieToSend = c;
					if (DEBUG) {
						System.out.println("Fetched Existing Session: " + session.toString());
					}
				}
			}
		}
		if (!foundCookie) { //we have to create the session
			UUID uuid = UUID.randomUUID();	//128 bits
			message = DEFAULT_MESSAGE;			//current time + 2 minutes
			end = (new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT; //64 bits
			//TODO location metadata will be appended later.
			
			session = new CS5300PROJ1SESSION(uuid.toString(), message, end);
			if (DEBUG) {
				System.out.println("Created a New Session: " + session.toString());
			}
			sessionDataTable.put(uuid.toString(), session);
			cookieToSend = new Cookie(CS5300PROJ1SESSION.COOKIE_NAME, uuid.toString());
		} else { // TODO: Kevin this is in the wrong closure, but I need to go
			session.incrementVersion();
			session.setEnd((new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT);
		}
		response.addCookie(cookieToSend);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println
		(
					"<!DOCTYPE html>\n" +
							"<html>\n" +
							"<head><title>kfc35 - ss2249 - hhc39 - CS5300 - Proj 1a</title></head>\n" +
							"<body>\n" +
							"<h1>" + session.getMessage() + "</h1>\n" +
							"<p>Simple servlet for testing.</p>\n" +
							"</body></html>"
							);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
