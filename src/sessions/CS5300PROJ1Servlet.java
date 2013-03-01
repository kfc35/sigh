package sessions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BasicSessionServlet
 */
@WebServlet("/") //MUST BE THE ROOT
public class CS5300PROJ1Servlet extends HttpServlet {

	private enum REQUEST{REFRESH, REPLACE, LOGOUT}

	public static boolean DEBUG = false;
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "Hello, User!";
	private ConcurrentHashMap<String, CS5300PROJ1Session> sessionDataTable = 
			new ConcurrentHashMap<String, CS5300PROJ1Session>();

	public static int servletNum = 0;
	public static final long EXPIRY_TIME_FROM_CURRENT = 1000 * 120; //2 minutes
	private Thread terminator = new Thread(new CS5300PROJ1Terminator(sessionDataTable));

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CS5300PROJ1Servlet() {
		super();
		servletNum++;
		terminator.start();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CS5300PROJ1Session session = null;
		/*Process session information if applicable*/

		session = execute(request.getCookies(), REQUEST.REFRESH, null);
		if (null == session) { //we have to create the session
			session = createSession(DEFAULT_MESSAGE);
		}

		populateJSP(REQUEST.REFRESH, request, response, session); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		CS5300PROJ1Session session = null;
		Enumeration<String> params = request.getParameterNames();
		String message = DEFAULT_MESSAGE;
		REQUEST type = REQUEST.LOGOUT;

		while(params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.equals("newMessage")) {
				message = request.getParameter("newMessage");
				type = REQUEST.REPLACE;
			}
		}

		session = execute(request.getCookies(), type, message);
		if (null == session && type == REQUEST.REPLACE) {

			// The case when expired cookies still come back
			session = createSession(DEFAULT_MESSAGE);
			session.setMessage(message);
		}
		populateJSP(type, request, response, session); 
	}

	/**
	 * 
	 * @param m - message
	 * Create a new session and add it to the session table
	 * @return session
	 */
	private synchronized CS5300PROJ1Session createSession(String m) {
		UUID uuid = UUID.randomUUID();
		CS5300PROJ1Session session = new CS5300PROJ1Session(uuid.toString(), m);
		if (DEBUG) {
			System.out.println("Created a New Session: " + session.toString());
		}
		sessionDataTable.put(uuid.toString(), session);
		return session;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 * @throws ServletException
	 * 
	 * A function that's brought out from the mess that was GET and POST
	 * 
	 */
	private void populateJSP(REQUEST type, HttpServletRequest request, HttpServletResponse response, CS5300PROJ1Session session) 
			throws IOException, ServletException {
		if (type == REQUEST.LOGOUT) {
			PrintWriter out = response.getWriter();
			out.println("Bye");
		} else {
			Cookie cookieToSend = new Cookie(CS5300PROJ1Session.COOKIE_NAME, session.getSessionId());
			cookieToSend.setMaxAge((int) (EXPIRY_TIME_FROM_CURRENT / 1000));
			response.addCookie(cookieToSend);

			getServletContext().setAttribute("message", session.getMessage());
			getServletContext().setAttribute("address", request.getRemoteAddr() + ":" + request.getRemotePort());
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			getServletContext().setAttribute("expires", dateFormat.format(new Date(session.getEnd())));

			RequestDispatcher rd = request.getRequestDispatcher("/CS5300PROJ1index.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * 
	 * @param cookies from the httprequest
	 * @param session that will be found
	 * @param type of the request GET will be REFRESH always, POST will either be REPLACE or LOGOUT 
	 * @param message that should replace the current message
	 * @return whether such a session exists or not
	 */
	private synchronized CS5300PROJ1Session execute(Cookie[] cookies, REQUEST type, String message) {
		CS5300PROJ1Session session = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(CS5300PROJ1Session.COOKIE_NAME)) {
					String sessionId = c.getValue();
					session = sessionDataTable.get(sessionId);
					if (session == null) { 
						/*this can happen if you stop the servlet, clearing the
						  concurrent hashmap, and then run it again -> Eclipse still has
						  the cookie! Make a new cookie now!*/
						break;
					}
					// If logout, then just remove the session
					if (type == REQUEST.LOGOUT) {
						sessionDataTable.remove(sessionId);
					} else {
						session.incrementVersion();
						session.setEnd((new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT);
						if (type == REQUEST.REPLACE) {
							session.setMessage(message);
						}
					}

					if (DEBUG) {
						System.out.println(type + " & Updated: " + session.toString());
					}
				}
			}
		}

		return session;
	}
}
