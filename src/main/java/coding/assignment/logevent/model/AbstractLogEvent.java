package coding.assignment.logevent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
abstract class AbstractLogEvent {
	protected String id;
	protected String type;
	protected String host;
}
