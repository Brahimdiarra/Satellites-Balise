package src.main.java.com.eventHandler;

import java.util.*;

public class EventHandler {
	
	Map<Class<? extends AbstractEvent>,Set<Object>> registry = new HashMap<Class<? extends AbstractEvent>,Set<Object>>();
	
	public void send (AbstractEvent event) {
		Set<Object> l = registry.get(event.getClass());
		if (l == null) return;
		Iterator<Object> itor = l.iterator();
		while (itor.hasNext()) {
			event.sendTo(itor.next());
		}
	}

	public void registerListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		Set<Object> set = registry.get(whichEventType);
		if (set == null) {
			set = new HashSet<Object>();
			registry.put(whichEventType, set);
		}
		set.add(listener);
	}
	public void unregisterListener(Class<? extends AbstractEvent> whichEventType, Object listener) {
		Set<Object> set = registry.get(whichEventType);
		if (set == null) return;
		set.remove(listener);
	}

}
