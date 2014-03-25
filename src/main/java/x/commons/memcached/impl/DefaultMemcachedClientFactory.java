package x.commons.memcached.impl;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;

import org.apache.commons.io.FileUtils;

import x.commons.memcached.MemcachedClient;
import x.commons.memcached.MemcachedClientFactory;

public class DefaultMemcachedClientFactory implements MemcachedClientFactory {
	
	private File configFile;
	
	private String defaultClientName = null;
	private Map<String, MemcachedClient> memcachedClients = 
			new HashMap<String, MemcachedClient>();
	
	public DefaultMemcachedClientFactory(String configFilePath) {
		if (configFilePath == null) {
			throw new IllegalArgumentException("Param 'configFilePath' mustn't be null!");
		}
		final String classpathPrefix = "classpath:";
		if (configFilePath.startsWith(classpathPrefix) || configFilePath.startsWith(classpathPrefix.toUpperCase())) {
			String relPath = configFilePath.substring(classpathPrefix.length());
			configFilePath = this.getClass().getResource(relPath).getPath().toString();
		}
		this.configFile = new File(configFilePath);
	}
	
	public void init() throws Exception {
		String content = FileUtils.readFileToString(configFile, "UTF-8");
		ConfigParser cp = new ConfigParser(content);
		content = null;
		cp.parse();
		
		defaultClientName = cp.getDefaultClient();
		
		List<ClientConfig> clients = cp.getClients();
		for(ClientConfig cc : clients) {
			String name = cc.getName();
			MemcachedClient mc = initClient(cc);
			memcachedClients.put(name, mc);
		}
	}
	
	private MemcachedClient initClient(ClientConfig clientConfig) throws Exception {
		List<HostConfig> hosts = clientConfig.getHosts();
		
		int[] weights = new int[hosts.size()];
		List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>(hosts.size());
		
		for(int i = 0; i < hosts.size(); i++) {
			HostConfig host = hosts.get(i);
			
			weights[i] = host.getWeight();
			
			InetSocketAddress address = new InetSocketAddress(
					InetAddress.getByName(host.getAddress()), host.getPort());
			addresses.add(address);
		}
		
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(addresses, weights);
		if(clientConfig.isUseBinaryProtocol()) {
			// use memcached binary protocol
			// if not set, default is text protocol
			builder.setCommandFactory(new BinaryCommandFactory());
		}
		if(clientConfig.isUseConsistencyHash()) {
			// use consistency hash
			// if not set, default is native hash
			builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		}
		builder.setConnectionPoolSize(clientConfig.getConnectionPoolSize());
		net.rubyeye.xmemcached.MemcachedClient clientProxy = builder.build();
		clientProxy.setOpTimeout(clientConfig.getOperationTimeout());
		MemcachedClient client = new DefaultMemcachedClient(clientProxy);
		
		return client;
	}
	
	public void shutdown() throws Exception {
		List<Exception> exps = new ArrayList<Exception>();
		for(Entry<String, MemcachedClient> entry : memcachedClients.entrySet()) {
			try {
				net.rubyeye.xmemcached.MemcachedClient proxy = ((DefaultMemcachedClient) entry.getValue()).getProxy();
				proxy.shutdown();
			} catch(Exception e) {
				exps.add(e);
			}
		}
		if(exps.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(Exception e : exps) {
				sb.append(e.getMessage() + "\n");
			}
			throw new Exception("Multiple exceptions thrown when shuting down memcached clients:\n" + sb.toString());
		}
	}

	@Override
	public MemcachedClient getDefaultMemcachedClient() {
		return this.getMemcachedClient(defaultClientName);
	}

	@Override
	public MemcachedClient getMemcachedClient(String name) {
		return this.memcachedClients.get(name);
	}

}
