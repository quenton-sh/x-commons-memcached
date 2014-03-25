package x.commons.memcached.impl;

class HostConfig {
	
	private String address;
	private int port;
	private int weight;

	HostConfig(String address, int port, int weight) {
		this.address = address;
		this.port = port;
		this.weight = weight;
	}

	String getAddress() {
		return address;
	}

	int getPort() {
		return port;
	}

	int getWeight() {
		return weight;
	}
	
}
