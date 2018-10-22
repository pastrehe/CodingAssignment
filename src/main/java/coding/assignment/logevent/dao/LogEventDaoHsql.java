package coding.assignment.logevent.dao;

import coding.assignment.dbutil.HsqldbUtil;
import coding.assignment.logevent.model.LogEvent;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class LogEventDaoHsql implements LogEventDao {

	/**
	 * Takes instance of LogEvent and stores
	 * it in database
	 *
	 * @param event LogEvent {@link LogEvent}
	 */
	@Override
	public void insert(LogEvent event) {
		try (Connection connection = HsqldbUtil.getConnection()) {

			String sql = "INSERT INTO EVENTS " +
					"(ID, DURATION, TYPE, HOST, ALERT) VALUES (?, ?, ?, ? ,?)";

			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, event.getId());
				ps.setLong(2, event.getDuration());
				ps.setString(3, event.getType());
				ps.setString(4, event.getHost());
				ps.setBoolean(5, event.isAlert());
				log.debug("Ready to execute statement [{}]", ps.toString());
				ps.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				log.error("Error while performing insert into database, {}", e.getMessage());
				throw new RuntimeException(e);
			}
		} catch (SQLException e) {
			log.error("Error in database connection [{}]", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
