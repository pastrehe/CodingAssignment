package coding.assignment.dbutil;

import coding.assignment.configuration.AppConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class HsqldbUtil {
	private static final String HSQL_CONNECTION_URL = AppConfig.getProperty("sqlConnectionUrl");
	private static final String HSQL_USERNAME = AppConfig.getProperty("sqlUserName");
	private static final String HSQL_PASSWORD = AppConfig.getProperty("sqlPassword");

	private HsqldbUtil() {
	}

	/**
	 * Creates and returns Connection to DB
	 * from DriverManager {@link DriverManager}
	 *
	 * @return Connection {@link Connection}
	 */
	public static Connection getConnection() {
		if (null == HSQL_CONNECTION_URL || null == HSQL_USERNAME || null == HSQL_PASSWORD) {
			log.error("Required parameter not fond in properties, hsqlConnectionUrl = [{}], hsqlUserName = [{}], hsqlPassword = [{}]",
					HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
			throw new RuntimeException();
		}
		Connection connection;
		try {
			connection = DriverManager.getConnection(HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
			log.debug("Connection to DB successfully obtained");
		} catch (SQLException e) {
			log.error("Error getting connection to database {}", e.getMessage());
			throw new RuntimeException(e);
		}
		return connection;
	}

	/**
	 * Creates a table in database, if it's not exists already
	 */
	public static void init() {
		String sql = "CREATE TABLE IF NOT EXISTS " +
				"EVENTS(ID VARCHAR(255), " +
				"DURATION BIGINT, " +
				"TYPE VARCHAR(255), " +
				"HOST VARCHAR(255), " +
				"ALERT BOOLEAN, " +
				"PRIMARY KEY (ID)) ";

		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				log.debug("Creating table if not exists with query [{}]", sql);
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			log.error("Error creating table in database, {}", e.getMessage());
			throw new RuntimeException();
		}
	}
}
