package coding.assignment.parser;

import coding.assignment.cache.LocalCache;
import coding.assignment.configuration.AppConfig;
import coding.assignment.logevent.dao.LogEventDao;
import coding.assignment.logevent.model.LogEvent;
import coding.assignment.logevent.model.RawLogEvent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class Parser implements Runnable {
	private static final Object monitor = new Object();
	private final Gson gson;
	private final LocalCache localCache;
	private final LogEventDao dao;
	private final BufferedReader reader;
	@Setter
	private boolean shutdown;

	public Parser(Gson gson, LocalCache localCache, LogEventDao dao, BufferedReader reader) {
		this.gson = gson;
		this.localCache = localCache;
		this.dao = dao;
		this.reader = reader;
	}

	/**
	  Method for processing Events -
	  checking cache, finding pairs and storing data in DB.
	 */
	@Override
	public void run() {
		Long maxDuration = 4L;
		try {
			maxDuration = Long.valueOf(AppConfig.getProperty("maxDuration"));
		} catch (NumberFormatException e) {
			log.error("Error parsing maxDuration option, using default [{}]", maxDuration);
		}
		long processed = 0L;
		long eventsInserted = 0L;
		while (!shutdown) {
			RawLogEvent event = getEvent();
			log.debug("Processing event [{}]", event);
			if (null == event) {
				log.debug("Got null event - EOF reached, thread exiting");
				break;
			}
			processed++;
			boolean contains = false;

			/*
			We need this "synchronized" block to avoid situation when
			threads trying to put in cache events with same ID - without synchronization
			it's possible because of interval between "contains" and "put" calls
			 */
			synchronized (monitor) {
				if (!localCache.contains(event)) {
					localCache.put(event);
				} else {
					contains = true;
				}
			}
			if (contains) {
				LogEvent logEvent = createLogEvent(event, maxDuration);
				log.debug("Pair found, constructed logEvent [{}]", logEvent);
				dao.insert(logEvent);
				eventsInserted++;
				localCache.remove(event);
			}
		}
		log.info("Parser [{}] finished, [{}] events processed, [{}] pairs inserted", Thread.currentThread().getName(), processed, eventsInserted);
	}

	/**
	 * Builds end returns LogEvent when pair of Start-Stop events found
	 * Also set "Alert" flag if duration is longer then limit
	 *
	 * @param event {@link RawLogEvent}
	 * @param maxDuration maximum non-critical duration {@link Long}
	 * @return LogEvent {@link LogEvent}
	 */
	private LogEvent createLogEvent(RawLogEvent event, Long maxDuration) {
		Long duration = localCache.getDuration(event);
		LogEvent.LogEventBuilder builder = LogEvent.builder()
				.id(event.getId())
				.duration(duration)
				.host(event.getHost())
				.type(event.getType());
		if (duration > maxDuration) builder.alert(true);
		return builder.build();
	}

	/**
	 * Reads one string line from source and creates RawLogEvent object
	 * from JSON in string
	 * @return RawLogEvent {@link RawLogEvent}
	 */
	private RawLogEvent getEvent() {
		String in;
		try {
			do {
				in = reader.readLine();
				log.debug("Got line from reader [{}]", in);
				if (null == in) {
					return null;
				}
			} while (in.length() == 0);
		} catch (IOException e) {
			log.error("Error reading data from source file, {}", e.getMessage());
			throw new RuntimeException();
		}
		try {
			return gson.fromJson(in, RawLogEvent.class);
		} catch (JsonSyntaxException e) {
			log.error("Error converting line to json structure, [{}]", in);
			return null;
		}
	}
}
