package x.commons.memcached.impl;

import x.commons.memcached.MemcachedClient;
import x.commons.memcached.MemcachedClientException;

public class DefaultMemcachedClient implements MemcachedClient {

	private net.rubyeye.xmemcached.MemcachedClient proxy;

	public DefaultMemcachedClient(net.rubyeye.xmemcached.MemcachedClient proxy) {
		this.proxy = proxy;
	}

	net.rubyeye.xmemcached.MemcachedClient getProxy() {
		return this.proxy;
	}

	@Override
	public boolean add(String key, int exp, Object data)
			throws MemcachedClientException {
		try {
			return this.proxy.add(key, exp, data);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public boolean set(String key, int exp, Object data)
			throws MemcachedClientException {
		try {
			return this.proxy.set(key, exp, data);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public boolean replace(String key, int exp, Object data)
			throws MemcachedClientException {
		try {
			return this.proxy.replace(key, exp, data);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public long incr(String key, long delta) throws MemcachedClientException {
		try {
			return this.proxy.incr(key, delta);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public long decr(String key, long delta) throws MemcachedClientException {
		try {
			return this.proxy.decr(key, delta);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public <T> T get(String key) throws MemcachedClientException {
		try {
			return this.proxy.get(key);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

	@Override
	public boolean delete(String key) throws MemcachedClientException {
		try {
			return this.proxy.delete(key);
		} catch (Exception e) {
			throw new MemcachedClientException(e);
		}
	}

}
