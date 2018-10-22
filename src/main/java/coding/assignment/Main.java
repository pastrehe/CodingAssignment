package coding.assignment;

import coding.assignment.dbutil.HsqldbUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String... args) {
		log.info("Application is starting");
		if (args.length == 0) {
			log.error("Please, specify path to log file as program argument");
			throw new IllegalArgumentException();
		}
		int processors = Runtime.getRuntime().availableProcessors() / 2;
		log.debug("Found [{}] processors to use", processors);
		ProcessRunner processRunner = new ProcessRunner(args[0], processors);
		HsqldbUtil.init();
		processRunner.startParsers();
	}
}
