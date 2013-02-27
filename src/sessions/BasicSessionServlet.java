package sessions;

import java.io.IOException;
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
public class BasicSessionServlet extends HttpServlet {
	public static boolean DEBUG = true;
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "Hello, User!";
	private ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable = 
			new ConcurrentHashMap<String, CS5300PROJ1SESSION>();

	public static int servletNum = 0;
	public static final long EXPIRY_TIME_FROM_CURRENT = 1000 * 6; //2 minutes
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
					session.incrementVersion();
					session.setEnd((new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT);

					foundCookie = true;
					if (DEBUG) {
						System.out.println("Fetched Existing Session & Updated: " + session.toString());
					}
				}
			}
		}
		if (!foundCookie) { //we have to create the session
			session = createSession(DEFAULT_MESSAGE);
		}

		populateJSP(request, response, session); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		CS5300PROJ1SESSION session = null;
		Enumeration<String> params = request.getParameterNames();
		String message = DEFAULT_MESSAGE;

		Cookie[] cookies = request.getCookies();

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

					while(params.hasMoreElements()) {
						String param = (String) params.nextElement();
						if (param.equals("newMessage")) {
							message = request.getParameter("newMessage");

							session.incrementVersion();
							session.setEnd((new Date()).getTime() + EXPIRY_TIME_FROM_CURRENT);

							session.setMessage(message);
							if (DEBUG) {
								System.out.println("Fetched Existing Session & Updated: " + session.toString());
							}

							populateJSP(request, response, session); 
							return;
						}
					}

					// This makes it a log out request
					//we have to create the session
					sessionDataTable.remove(sessionId);
					session = createSession(DEFAULT_MESSAGE);
					populateJSP(request, response, session); 
					return;
				}
			}
		}
	}

	/**
	 * 
	 * @param m - message
	 * Create a new session and add it to the session table
	 * @return session
	 */
	private CS5300PROJ1SESSION createSession(String m) {
		UUID uuid = UUID.randomUUID();
		CS5300PROJ1SESSION session = new CS5300PROJ1SESSION(uuid.toString(), m);
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
	private void populateJSP(HttpServletRequest request, HttpServletResponse response, CS5300PROJ1SESSION session) 
			throws IOException, ServletException {
		Cookie cookieToSend = new Cookie(CS5300PROJ1SESSION.COOKIE_NAME, session.getSessionId());
		cookieToSend.setMaxAge((int) (EXPIRY_TIME_FROM_CURRENT / 1000));
		response.addCookie(cookieToSend);

		//TODO which address?
		getServletContext().setAttribute("message", session.getMessage());
		getServletContext().setAttribute("address", request.getRemoteAddr() + ":" + request.getRemotePort());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		getServletContext().setAttribute("expires", dateFormat.format(new Date(session.getEnd())));

		RequestDispatcher rd = request.getRequestDispatcher("/first.jsp");
		rd.forward(request, response);
	}

}
