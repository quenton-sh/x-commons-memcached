package x.commons.memcached.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import x.commons.memcached.MemcachedClient;
import x.commons.memcached.MemcachedClientFactory;

public class DefaultMemachedImplTest {
	
	private static DefaultMemcachedClientFactory defaultFactory;

	@BeforeClass
	public static void init() throws Exception {
		defaultFactory = new DefaultMemcachedClientFactory("classpath:/memcached-test-config.xml");
		defaultFactory.init();
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		defaultFactory.shutdown();
	}
	
	@Test
	public void test() throws Exception {
		MemcachedClientFactory factory = defaultFactory;
		MemcachedClient defaultClient = factory.getDefaultMemcachedClient();
		MemcachedClient client1 = factory.getMemcachedClient("client1");
		MemcachedClient client2 = factory.getMemcachedClient("client2");
		
		assertTrue(client1 != null && client2 != null);
		assertTrue(defaultClient == client1);
		assertTrue(client1 != client2);
		
		String s11 = UUID.randomUUID().toString();
		byte[] d11 = s11.getBytes();
		String s12 = UUID.randomUUID().toString();
		byte[] d12 = s12.getBytes();
		String s21 = UUID.randomUUID().toString();
		byte[] d21 = s21.getBytes();
		
		// set
		boolean b = client1.set("key1", 20, d11);
		assertTrue(b);
		b = client1.set("key2", 20, d12);
		assertTrue(b);
		b = client2.set("key1", 20, d21);
		assertTrue(b);
		
		// get
		byte[] getd11 = client1.get("key1");
		byte[] getd12 = client1.get("key2");
		byte[] getd21 = client2.get("key1");
		assertTrue(Arrays.equals(d11, getd11));
		assertTrue(Arrays.equals(d12, getd12));
		assertTrue(Arrays.equals(d21, getd21));
		
		// delete
		b = client1.delete("key1");
		assertTrue(b);
		getd11 = client1.get("key1");
		assertTrue(getd11 == null);
		
		// incr
		b = client1.set("key3", 0, "1");
		assertTrue(b);
		long l = client1.incr("key3", 5);
		assertTrue(l == 6);
		String s = client1.get("key3");
		assertEquals("6", s);
		
		// decr
		b = client1.set("key4", 0, "12");
		assertTrue(b);
		l = client1.decr("key4", 5);
		assertTrue(l == 7);
		s = client1.get("key4");
		assertEquals("7", s);
		
		// expire
		client2.set("key5", 1, "123");
		s = client2.get("key5");
		assertTrue(s != null);
		Thread.sleep(1000);
		s = client2.get("key5");
		assertTrue(s == null);
	}
}
