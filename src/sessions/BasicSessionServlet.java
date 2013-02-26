package sessions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

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
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "Hello, User!";
	private ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable = 
			new ConcurrentHashMap<String, CS5300PROJ1SESSION>();
	
	public static int servletNum = 0;
	private int myNum;
	private int numSessions = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BasicSessionServlet() {
        super();
        Terminator terminator = new Terminator(sessionDataTable);
        terminator.run();
        myNum = servletNum++;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		CS5300PROJ1SESSION[] cookies = (CS5300PROJ1SESSION[]) request.getCookies();
		String message = null;
		CS5300PROJ1SESSION cookie = null;
		if (cookies != null) { //This is a first client request
			incrementNumSessions();
			cookie = new CS5300PROJ1SESSION(getSessionId(), DEFAULT_MESSAGE, System.currentTimeMillis() + 5000);
		}
		else { //Redisplay Session Msg, Update session expiration time
			cookie = cookies[cookies.length - 1];
			cookie.incrementVersion();
			cookie.setEnd(System.currentTimeMillis() + 5000);
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
