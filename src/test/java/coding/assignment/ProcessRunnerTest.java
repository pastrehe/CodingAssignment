package coding.assignment;

import coding.assignment.cache.CacheConcurrentHashMap;
import coding.assignment.dbutil.HsqldbUtil;
import coding.assignment.logevent.dao.LogEventDaoHsql;
import coding.assignment.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedReader;
import java.io.FileReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProcessRunner.class, FileReader.class, Thread.class, LogEventDaoHsql.class, HsqldbUtil.class})
public class ProcessRunnerTest {

	@Test
	public void startParsers() throws Exception {

		FileReader fileReader = PowerMockito.mock(FileReader.class);
		Thread thread = PowerMockito.mock(Thread.class);

		PowerMockito.whenNew(FileReader.class).withAnyArguments().thenReturn(fileReader);
		PowerMockito.whenNew(Thread.class).withAnyArguments().thenReturn(thread);
		PowerMockito.mockStatic(HsqldbUtil.class);
		PowerMockito.when(HsqldbUtil.getConnection()).thenReturn(null);

		PowerMockito.whenNew(LogEventDaoHsql.class).withAnyArguments().thenReturn(null);

		ProcessRunner processRunner = new ProcessRunner("", 1);
		Parser[] parsers = processRunner.startParsers();

		Assert.assertNotNull(parsers[0]);

	}

	@Test
	public void getReader() throws Exception {
		FileReader fileReader = PowerMockito.mock(FileReader.class);
		PowerMockito.whenNew(FileReader.class).withAnyArguments().thenReturn(fileReader);

		PowerMockito.whenNew(CacheConcurrentHashMap.class).withAnyArguments().thenReturn(null);
		PowerMockito.whenNew(LogEventDaoHsql.class).withAnyArguments().thenReturn(null);

		ProcessRunner processRunner = new ProcessRunner("", 1);
		BufferedReader bufferedReader = processRunner.getReader("");

		Assert.assertNotNull(bufferedReader);

	}
}