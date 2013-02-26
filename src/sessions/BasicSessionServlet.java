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
	private static ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable = 
			new ConcurrentHashMap();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BasicSessionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cookie[] cookies = request.getCookies();
		String message = null;
		if (cookies != null) { //This is a first client request
			message = DEFAULT_MESSAGE;
		}
		else { //Redisplay Session Msg, Update session expiration time
			
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println
		(
					"<!DOCTYPE html>\n" +
							"<html>\n" +
							"<head><title>kfc35 - ss2249 - hhc39 - CS5300 - Proj 1a</title></head>\n" +
							"<body>\n" +
							"<h1>" + message + "</h1>\n" +
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
