package coding.assignment.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class AppConfig {

	private static final String RESOURCE_NAME = "application.properties";
	private static final Properties PROPS = new Properties();

	private AppConfig() {
	}

	static {
		try (InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME)) {
			if (null != resourceStream) {
				PROPS.load(resourceStream);
				log.debug("Application properties loaded: [{}]", PROPS.toString());
			} else {
				log.error("Can't load properties - file not found?");
				throw new FileNotFoundException();
			}
		} catch (IOException e) {
			log.error("Error loading properties from file [{}]", RESOURCE_NAME);
			throw new RuntimeException();
		}
	}

	/**
	 * Returns Property Value for required Key
	 *
	 * @param property {@link java.util.Properties} loaded from file
	 * @return {@link java.lang.String} containing value
	 */
	public static String getProperty(String property) {
		return PROPS.getProperty(property);
	}
}
