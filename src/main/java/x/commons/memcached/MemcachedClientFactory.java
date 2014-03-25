package x.commons.memcached;

public interface MemcachedClientFactory {

	public MemcachedClient getDefaultMemcachedClient();
	
	public MemcachedClient getMemcachedClient(String name);
}
