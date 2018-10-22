package coding.assignment.logevent.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Event created from two RawLogEvents {@link RawLogEvent}
 * Contains duration of event
 * and alert, if duration longer then allowed
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LogEvent extends AbstractLogEvent {
	Long duration;
	boolean alert;

	@Builder
	public LogEvent(String id, String type, String host, Long duration, boolean alert) {
		super(id, type, host);
		this.duration = duration;
		this.alert = alert;
	}
}
