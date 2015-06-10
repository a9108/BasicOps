package basic.tools;

import java.util.Calendar;
import java.util.LinkedList;

public class Speeder {
	private LinkedList<Long> events = new LinkedList<Long>();
	private long interval;
	private long starttime;

	public Speeder(long interval) {
		starttime = Calendar.getInstance().getTimeInMillis();
		this.interval = interval;
	}

	public synchronized void trigger() {
		events.addLast(Calendar.getInstance().getTimeInMillis());
	}

	public synchronized double getSpeed() {
		// Collections.sort(events);
		long now = Calendar.getInstance().getTimeInMillis();
		int size = events.size();
		for (; !events.isEmpty() && events.getFirst() < now - interval; events
				.removeFirst())
			size--;
		long curint = Math.min(now - starttime, interval);
		return (size + 0.0) / curint * 1000;
	}
}
