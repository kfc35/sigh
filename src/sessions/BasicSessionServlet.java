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
	private int myNum;
	private int numSessions = 0;
	private static final long EXPIRY_TIME_FROM_CURRENT = 1000 * 120; //2 minutes
			new ConcurrentHashMap();
	private static Thread terminator = new Thread(new Terminator(sessionDataTable));
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BasicSessionServlet() {
        super();
        myNum = servletNum++;
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
		String message = null;
		long end = 0;
		
		boolean foundCookie = false;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(CS5300PROJ1SESSION.COOKIE_NAME)) {
					String sessionId = cookies[i].getValue();
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
					if (DEBUG) {
						System.out.println("Fetched Existing Session: " + session.toString());
					}
				}
			}
		}
		if (!foundCookie) { //we have to create the session
			UUID uuid = UUID.randomUUID();	//128 bits
			message = DEFAULT_MESSAGE;			
			int version = 0; //32 bits
			//current time + 2 minutes
			end = (new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT; //64 bits
			//TODO location metadata will be appended later.
			
			StringBuilder sb = new StringBuilder();
			sb.append(uuid.toString()).append(";");
			sb.append(version).append(";");
			sb.append(message).append(";");
			sb.append(end);
			CS5300PROJ1SESSION session = new CS5300PROJ1SESSION(sb.toString());
			if (DEBUG) {
				System.out.println("Created a New Session: " + session.toString());
			}
			sessionDataTable.put(uuid.toString(), session);
			response.addCookie(new Cookie(CS5300PROJ1SESSION.COOKIE_NAME, uuid.toString()));
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println
		(
					"<!DOCTYPE html>\n" +
							"<html>\n" +
							"<head><title>kfc35 - ss2249 - hhc39 - CS5300 - Proj 1a</title></head>\n" +
							"<body>\n" +
							"<h1>" + cookie.getMessage() + "</h1>\n" +
							"<p>Simple servlet for testing.</p>\n" +
							"</body></html>"
							);
		response.addCookie(cookie);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public String getSessionId() {
		StringBuilder sb = new StringBuilder();
		sb.append(myNum).append(' ');
		sb.append(numSessions);
		return sb.toString();
	}
	
	public void incrementNumSessions() {
		numSessions++;
	}

}
