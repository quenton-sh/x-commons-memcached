package x.commons.memcached;

public interface MemcachedClient {

	public boolean add(String key, int exp, Object data) throws MemcachedClientException;

	public boolean set(String key, int exp, Object data) throws MemcachedClientException;

	public boolean replace(String key, int exp, Object data) throws MemcachedClientException;

	public long incr(String key, long delta) throws MemcachedClientException;

	public long decr(String key, long delta) throws MemcachedClientException;
	
	public <T> T get(String key) throws MemcachedClientException;

	public boolean delete(String key) throws MemcachedClientException;
	
}
