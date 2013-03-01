package sessions;

import java.util.Date;

import javax.servlet.http.Cookie;

public class CS5300PROJ1Session extends Cookie {
 
	/** Default */
	private static final long serialVersionUID = 1L;
	public static String COOKIE_NAME = "CS5300PROJ1SESSION";

	//TODO 512 byte restriction on session state value
	private String sessionId;
	private String message;
	private long end;
	
	public CS5300PROJ1Session(String session, String m, long e) {
		super(session, m);
		sessionId = session;
		if (m.length() > 450) {
			message = m.substring(0, 450);
		} else {
			message = m;
		}
		end = e;
		this.setVersion(0);
	}
	
	public CS5300PROJ1Session(String session, String m) {
		this(session, m, (new Date()).getTime() + CS5300PROJ1Servlet.EXPIRY_TIME_FROM_CURRENT);
	}
	
	public CS5300PROJ1Session(String s) {
		super(COOKIE_NAME, s);
		String[] args = s.split(";");
		sessionId = args[0];
		setVersion(Integer.parseInt(args[1]));
		message = args[2];
		end = Long.parseLong(args[3]);
	}
	
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (message.length() > 450) {
			this.message = message.substring(0, 450);
		} else {
			this.message = message;
		}
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	public long getEnd() {
		return end;
	}
	
	public void incrementVersion() {
		this.setVersion(this.getVersion() + 1);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(sessionId).append(";");
		sb.append(getVersion()).append(";");
		sb.append(message).append(";");
		sb.append(end);
		return sb.toString();
	}
}
