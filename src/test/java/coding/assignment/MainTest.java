package coding.assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Main.class)
public class MainTest {

	@Test(expected = IllegalArgumentException.class)
	public void main() {
		Main.main();
	}
}