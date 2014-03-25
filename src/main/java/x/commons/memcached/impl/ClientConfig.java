package x.commons.memcached.impl;

import java.util.List;

class ClientConfig {
	
	private String name;
	private boolean useBinaryProtocol;
	private boolean useConsistencyHash;
	private int connectionPoolSize;
	private int operationTimeout;
	private List<HostConfig> hosts;

	ClientConfig(String name, boolean useBinaryProtocol, boolean useConsistencyHash,
			int connectionPoolSize, int operationTimeout, List<HostConfig> hosts) {
		this.name = name;
		this.useBinaryProtocol = useBinaryProtocol;
		this.useConsistencyHash = useConsistencyHash;
		this.connectionPoolSize = connectionPoolSize;
		this.operationTimeout = operationTimeout;
		this.hosts = hosts;
	}

	String getName() {
		return name;
	}

	boolean isUseBinaryProtocol() {
		return useBinaryProtocol;
	}

	boolean isUseConsistencyHash() {
		return useConsistencyHash;
	}

	int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	int getOperationTimeout() {
		return operationTimeout;
	}
	
	List<HostConfig> getHosts() {
		return hosts;
	}
	
}
