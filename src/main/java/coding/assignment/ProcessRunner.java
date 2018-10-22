package coding.assignment;

import coding.assignment.cache.CacheConcurrentHashMap;
import coding.assignment.cache.LocalCache;
import coding.assignment.logevent.dao.LogEventDao;
import coding.assignment.logevent.dao.LogEventDaoHsql;
import coding.assignment.parser.Parser;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Slf4j
class ProcessRunner {
	private final String path;
	private int processors = 1;

	ProcessRunner(String path, int cpuNum) {
		if (cpuNum > 1) {
			processors = cpuNum;
		}
		this.path = path;
	}

	/**
	 * Creates Parsers threads, and starts them.
	 * Returns array, containing references to running Parsers.
	 * It's useful if we ever will need to control those threads,
	 * for example - gracefully manually shutdown them.
	 *
	 * @return array of {@link Parser}
	 */
	Parser[] startParsers() {
		Parser[] parsers = new Parser[processors];
		Gson gson = new Gson();
		LocalCache localCache = new CacheConcurrentHashMap();
		BufferedReader reader = getReader(path);
		for (int i = 0; i < processors; i++) {
			LogEventDao dao = new LogEventDaoHsql();
			Parser parser = new Parser(gson, localCache, dao, reader);
			parsers[i] = parser;
			log.info("Starting parser [{}]", i);
			Thread thread = new Thread(parser);
			thread.setName("Parser-" + i);
			thread.start();
		}
		return parsers;
	}

	/**
	 * Creates and returning BufferedReader for specified path.
	 * This reader will be used as single data source for all Parsers
	 *
	 * @param path Path to file
	 * @return reader {@link BufferedReader}
	 */
	BufferedReader getReader(String path) {
		BufferedReader reader;
		log.debug("Trying to get reader for required path [{}]", path);
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			log.error("File [{}] not found", path);
			throw new RuntimeException(e);
		}
		log.debug("Success");
		return reader;
	}
}
