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
public class Terminator implements Runnable {
	private ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable;

	public Terminator(ConcurrentHashMap<String, CS5300PROJ1SESSION> sessionDataTable) {
		this.sessionDataTable = sessionDataTable;
	}

	@Override
	public void run() {
		try{
			this.wait(1000);
			
			HashMap<String, CS5300PROJ1SESSION> dup = new HashMap<String, CS5300PROJ1SESSION> (sessionDataTable);
			for (Entry<String, CS5300PROJ1SESSION> e: dup.entrySet()) {
				CS5300PROJ1SESSION cookie = e.getValue();
				if (cookie.getEnd() < System.currentTimeMillis()) {
					sessionDataTable.remove(e.getKey());
				}
			}
		} catch (InterruptedException e) {
		}
	}

}
