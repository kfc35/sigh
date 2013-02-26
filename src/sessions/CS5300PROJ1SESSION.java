package sessions;

import javax.servlet.http.Cookie;

public class CS5300PROJ1SESSION extends Cookie {
 
	/** Default */
	private static final long serialVersionUID = 1L;

	//TODO 512 byte restriction on session state value
	private String sessionId;
	private String message;
	private long end;
	
	public CS5300PROJ1SESSION(String session, String m, long e) {
		super(session, m);
		sessionId = session;
		message = m;
		end = e;
		this.setVersion(0);
	}
	
	public CS5300PROJ1SESSION(String s) {
		super(s, s);
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
		this.message = message;
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
