package x.commons.memcached;

@SuppressWarnings("serial")
public class MemcachedClientException extends Exception {

	public MemcachedClientException(Exception e) {
		super(e);
	}
}
