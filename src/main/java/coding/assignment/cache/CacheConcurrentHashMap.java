package coding.assignment.cache;

import coding.assignment.logevent.model.RawLogEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CacheConcurrentHashMap implements LocalCache {
	private static final Map<String, Long> CACHE_MAP = new ConcurrentHashMap<>();

	/**
	 * Checks if data for event with same ID
	 * already exists in cache
	 *
	 * @param event {@link RawLogEvent}
	 * @return boolean
	 */
	@Override
	public boolean contains(RawLogEvent event) {
		if (null == event) {
			log.error("Trying to check null object from cache");
			return false;
		}
		if (null == event.getId()) {
			log.error("Required field [ID] is null");
			return false;
		}
		return CACHE_MAP.containsKey(event.getId());
	}

	/**
	 * Calculates and returns duration of event
	 * based on data stored in cache and data in the
	 * input parameter.
	 * Returns null if no data found in cache
	 *
	 * @param event {@link RawLogEvent}
	 * @return Long
	 */
	@Override
	public Long getDuration(RawLogEvent event) {
		if (null == event) {
			log.error("Trying to check null object in cache");
			return null;
		}
		Long duration = null;
		if (CACHE_MAP.containsKey(event.getId())) {
			Long ts = event.getTimestamp();
			Long cachedTs = CACHE_MAP.get(event.getId());
			duration = Math.abs(cachedTs - ts);
			log.debug("Calculated duration [{}] for event with id [{}]", duration, event.getId());
		} else {
			log.error("LocalCache doesn't contain requested object [{}] while calculating duration", event);
		}
		return duration;
	}

	/**
	 * Stores event info in cache
	 * event's ID is the key, event's timestamp is value
	 *
	 * @param event {@link RawLogEvent}
	 */
	@Override
	public void put(RawLogEvent event) {
		if (null == event) {
			log.error("Trying to put null into cache");
			return;
		}
		if (null == event.getId() || null == event.getTimestamp()) {
			log.error("Required fields are empty, id = [{}], timeStamp = [{}]", event.getId(), event.getTimestamp());
			return;
		}
		CACHE_MAP.put(event.getId(), event.getTimestamp());
		log.debug("Event id [{}] stored in cache", event.getId());
	}

	/**
	 * Removes data related for corresponding event
	 * from cache
	 *
	 * @param event {@link RawLogEvent}
	 */
	@Override
	public void remove(RawLogEvent event) {
		if ((null != event) && (null != event.getId())) {
			CACHE_MAP.remove(event.getId());
			log.debug("Event id [{}] removed from cache", event.getId());
		} else {
			if (null == event) {
				log.error("Trying to remove null from cache");
			} else {
				log.error("Required field ID is null");
			}
		}
	}
}
