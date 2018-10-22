package coding.assignment.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AppConfig.class})
public class AppConfigTest {

	@Test
	public void getProperty() {
		Assert.assertNull(AppConfig.getProperty("none"));
		Assert.assertEquals("SA", AppConfig.getProperty("sqlUserName"));
	}
}