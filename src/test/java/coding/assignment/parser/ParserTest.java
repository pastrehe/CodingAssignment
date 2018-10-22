package coding.assignment.parser;

import coding.assignment.cache.LocalCache;
import coding.assignment.cache.CacheConcurrentHashMap;
import coding.assignment.logevent.model.LogEvent;
import coding.assignment.logevent.model.RawLogEvent;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParserTest {

	@Test
	public void createLogEvent() throws InvocationTargetException, IllegalAccessException {
		LocalCache localCache = Mockito.mock(CacheConcurrentHashMap.class);
		Mockito.when(localCache.getDuration(Mockito.any())).thenReturn(10L);
		Parser parser = new Parser(null, localCache, null, null);

		Method method = Whitebox.getMethod(Parser.class, "createLogEvent", RawLogEvent.class, Long.class);
		method.setAccessible(true);

		Long maxDuration = 4L;

		RawLogEvent event = new RawLogEvent();
		event.setId("ID");
		event.setTimestamp(10L);
		event.setHost("host");
		event.setType("APPLICATION");

		LogEvent logEvent = (LogEvent) method.invoke(parser, event, maxDuration);
		Assert.assertNotNull(logEvent);
		Assert.assertTrue(logEvent.isAlert());
		Assert.assertEquals("ID", logEvent.getId());
		Assert.assertEquals("host", logEvent.getHost());
		Assert.assertEquals("APPLICATION", logEvent.getType());
	}

	@Test
	public void getEvent() throws InvocationTargetException, IllegalAccessException, IOException {
		Gson gson = new Gson();
		LocalCache localCache = Mockito.mock(CacheConcurrentHashMap.class);
		BufferedReader reader = Mockito.mock(BufferedReader.class);
		Parser parser = new Parser(gson, localCache, null, reader);

		Method method = Whitebox.getMethod(Parser.class, "getEvent");
		method.setAccessible(true);

		String validJson = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\"host\":\"12345\", \"timestamp\":1491377495212}";
		String invalidJson = "\"type\":\"APPLICATION_LOG\"\"host\":\"12345\", \"timestamp\":1491377495212}";

		Mockito.when(reader.readLine()).thenReturn(validJson);
		RawLogEvent event = (RawLogEvent) method.invoke(parser);
		Assert.assertNotNull((event));
		Assert.assertEquals("scsmbstgra", event.getId());
		Assert.assertEquals("STARTED", event.getState().toString());
		Assert.assertEquals("APPLICATION_LOG", event.getType());
		Assert.assertEquals("12345", event.getHost());
		Assert.assertEquals(1491377495212L, (long) event.getTimestamp());

		Mockito.when(reader.readLine()).thenReturn(invalidJson);
		event = (RawLogEvent) method.invoke(parser);
		Assert.assertNull(event);
	}

	@Test
	public void setShutdown() throws IllegalAccessException {
		Parser parser = new Parser(null, null, null, null);

		Field field = Whitebox.getField(Parser.class, "shutdown");
		field.setAccessible(true);
		Assert.assertFalse((boolean) field.get(parser));

		parser.setShutdown(true);
		Assert.assertTrue((boolean) field.get(parser));
	}

}