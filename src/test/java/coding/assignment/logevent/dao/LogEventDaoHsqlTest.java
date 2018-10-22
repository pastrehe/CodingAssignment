package coding.assignment.logevent.dao;

import coding.assignment.dbutil.HsqldbUtil;
import coding.assignment.logevent.model.LogEvent;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;

@RunWith(PowerMockRunner.class)
public class LogEventDaoHsqlTest {

	private static final String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:logdb";
	private static final String HSQL_USERNAME = "SA";
	private static final String HSQL_PASSWORD = "";
	private static Connection connection;


	@BeforeClass
	public static void initDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		connection = DriverManager.getConnection(HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
		String sql = "CREATE TABLE IF NOT EXISTS EVENTS(ID VARCHAR(255), DURATION BIGINT, TYPE VARCHAR(255), HOST VARCHAR(255), ALERT BOOLEAN)";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
			connection.commit();
		}
	}


	@AfterClass
	public static void destroy() throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DROP TABLE EVENTS");
			connection.commit();
		}
		connection.close();
	}


	@Test
	@PrepareForTest(HsqldbUtil.class)
	public void insert() throws Exception {
		PowerMockito.mockStatic(HsqldbUtil.class);
		Connection con = DriverManager.getConnection(HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
		PowerMockito.when(HsqldbUtil.getConnection()).thenReturn(con);
		LogEventDao dao = new LogEventDaoHsql();
		LogEvent event = new LogEvent("ID", "TYPE", "HOST", 10L, true);
		dao.insert(event);

		String testSql = "SELECT ID, TYPE, HOST, DURATION, ALERT FROM EVENTS WHERE ID = 'ID'";

		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(testSql);
			if (resultSet.next()) {
				Assert.assertEquals("ID", resultSet.getString(1));
				Assert.assertEquals("TYPE", resultSet.getString(2));
				Assert.assertEquals("HOST", resultSet.getString(3));
				Assert.assertEquals(10L, resultSet.getLong(4));
				Assert.assertTrue(resultSet.getBoolean(5));
			} else {
				Assert.fail("No data in ResultSet");
			}
		}

	}
}