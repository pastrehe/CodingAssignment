package coding.assignment.dbutil;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HsqldbUtil.class})
public class HsqldbUtilTest {

	private static final String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:logdb";
	private static final String HSQL_USERNAME = "SA";
	private static final String HSQL_PASSWORD = "";
	private static Connection connection;

	@BeforeClass
	public static void initDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		connection = DriverManager.getConnection(HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
	}


	@AfterClass
	public static void destroy() throws SQLException {
		connection.close();
	}


	@Test
	public void getConnection() throws SQLException {
		PowerMockito.mockStatic(DriverManager.class);
		PowerMockito.when(DriverManager.getConnection(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(connection);
		Assert.assertEquals(HsqldbUtil.getConnection(), connection);
	}

	@Test
	public void init() throws Exception {
		PowerMockito.mockStatic(HsqldbUtil.class);
		PowerMockito.when(HsqldbUtil.getConnection()).thenReturn(connection);
		PowerMockito.doCallRealMethod().when(HsqldbUtil.class, "init");


		String testSql = "Select Count(COLUMN_NAME) From INFORMATION_SCHEMA.SYSTEM_COLUMNS " +
				"Where TABLE_NAME = 'EVENTS' ";

		HsqldbUtil.init();

		Connection con = DriverManager.getConnection(HSQL_CONNECTION_URL, HSQL_USERNAME, HSQL_PASSWORD);
		try (Statement statement = con.createStatement()) {
			ResultSet resultSet = statement.executeQuery(testSql);
			if (resultSet.next()) {
				Assert.assertEquals(5, resultSet.getInt(1));
			} else {
				Assert.fail();
			}
		}

	}
}