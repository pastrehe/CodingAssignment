package coding.assignment.logevent.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Reflects JSON found in the input
 * into RawLogEvent object
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RawLogEvent extends AbstractLogEvent {
	private State state;
	private Long timestamp;

	public enum State {
		STARTED,
		FINISHED
	}
}
