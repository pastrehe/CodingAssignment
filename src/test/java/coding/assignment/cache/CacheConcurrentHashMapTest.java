package coding.assignment.cache;

import coding.assignment.logevent.model.RawLogEvent;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CacheConcurrentHashMapTest {

	private static final LocalCache localCache = new CacheConcurrentHashMap();
	private static RawLogEvent event1;
	private static RawLogEvent event2;
	private static RawLogEvent event3;
	private static RawLogEvent event4;


	@BeforeClass
	public static void setup() {
		event1 = new RawLogEvent();
		event2 = new RawLogEvent();
		event3 = new RawLogEvent();
		event4 = new RawLogEvent();

		event1.setTimestamp(10L);
		event1.setId("ID1");

		event2.setId("ID1");
		event2.setTimestamp(20L);

		event3.setId("ID3");
		event3.setTimestamp(30L);

		localCache.put(event1);
	}

	@Test
	public void contains() {
		RawLogEvent event = new RawLogEvent();
		event.setId("ID4");
		Assert.assertTrue(localCache.contains(event1));
		Assert.assertFalse(localCache.contains(event));
		Assert.assertFalse(localCache.contains(null));
	}

	@Test
	public void put() {
		RawLogEvent event = new RawLogEvent();
		localCache.put(event);
		Assert.assertFalse(localCache.contains(event));
		event.setId("ID4");
		localCache.put(event);
		Assert.assertFalse(localCache.contains(event));
		event.setTimestamp(30L);
		localCache.put(event);
		Assert.assertTrue(localCache.contains(event));
		localCache.put(null);
	}


	@Test
	public void getDuration() {
		Assert.assertNull(localCache.getDuration(event3));
		Assert.assertNull(localCache.getDuration(null));
		Assert.assertEquals(0, Long.compare(10L, localCache.getDuration(event2)));
	}

	@Test
	public void remove() {
		RawLogEvent event = new RawLogEvent();
		event.setId("IDRemove");
		event.setTimestamp(30L);
		localCache.put(event);
		Assert.assertTrue(localCache.contains(event));
		localCache.remove(event);
		Assert.assertFalse(localCache.contains(event));
	}
}