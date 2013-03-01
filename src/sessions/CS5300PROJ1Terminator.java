package sessions;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.*;
/**
 * 
 * @author sweet
 * This class is responsible to removing over-time sessions from the session table
 *
 */
public class CS5300PROJ1Terminator implements Runnable {
	private ConcurrentHashMap<String, CS5300PROJ1Session> sessionDataTable;

	public CS5300PROJ1Terminator(ConcurrentHashMap<String, CS5300PROJ1Session> sessionDataTable) {
		this.sessionDataTable = sessionDataTable;
	}

	@Override
	public synchronized void run() {
		while(true) {
			try{
				this.wait(1000 * 120); // Runs every 2 minutes
				System.out.println("Terminator check");
				
				HashMap<String, CS5300PROJ1Session> dup = new HashMap<String, CS5300PROJ1Session> (sessionDataTable);
				for (Entry<String, CS5300PROJ1Session> e: dup.entrySet()) {
					CS5300PROJ1Session session = e.getValue();
					if (session.getEnd() < System.currentTimeMillis()) {
						System.out.println("Removing the session: "+ session.getSessionId());
						sessionDataTable.remove(e.getKey());
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
