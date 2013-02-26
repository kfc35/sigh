package sessions;

import javax.servlet.http.Cookie;

public class CS5300PROJ1SESSION extends Cookie {
 
	/** Default */
	private static final long serialVersionUID = 1L;

	//TODO 512 byte restriction on session state value
	private String sessionId;
	private String message;
	
	public CS5300PROJ1SESSION(String s, String msg) {
		super(s, s);
		String[] args = s.split(";");
		sessionId = args[0];
		setVersion(Integer.parseInt(args[1]));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(sessionId).append(";");
		sb.append(getVersion());
		return sb.toString();
	}

}
