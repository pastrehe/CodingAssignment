package coding.assignment.cache;

import coding.assignment.logevent.model.RawLogEvent;

public interface LocalCache {
	boolean contains(RawLogEvent event);
	Long getDuration(RawLogEvent event);
	void put(RawLogEvent event);
	void remove(RawLogEvent event);
}
