package x.commons.memcached.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

class ConfigParser {
	
	private String xmlContent;
	
	private String defaultClient = null;
	private List<ClientConfig> clients = null;
	
	ConfigParser(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	@SuppressWarnings("unchecked")
	void parse() throws Exception {
		Document document = DocumentHelper.parseText(xmlContent);
		Element root = document.getRootElement();
		Element defaultElement = root.element("default-client");
		defaultClient = defaultElement.getTextTrim();
		
		clients = new ArrayList<ClientConfig>();
		Iterator<Element> clientIter = root.elementIterator("client");
		while(clientIter.hasNext()) {
			Element clientElem = clientIter.next();
			String name = clientElem.attributeValue("name");
			boolean useBinaryProtocol = Boolean.valueOf(clientElem.attributeValue("useBinaryProtocol"));
			boolean useConsistencyHash = Boolean.valueOf(clientElem.attributeValue("useConsistencyHash"));
			int connectionPoolSize = Integer.valueOf(clientElem.attributeValue("connectionPoolSize"));
			int operationTimeout = Integer.valueOf(clientElem.attributeValue("operationTimeout"));
			
			List<HostConfig> hosts = new ArrayList<HostConfig>();
			Iterator<Element> hostIter = clientElem.elementIterator("host");
			while(hostIter.hasNext()) {
				Element hostElem = hostIter.next();
				String address = hostElem.attributeValue("address");
				int port = Integer.valueOf(hostElem.attributeValue("port"));
				int weight = Integer.valueOf(hostElem.attributeValue("weight"));
				HostConfig host = new HostConfig(address, port, weight);
				hosts.add(host);
			}
			
			ClientConfig client = new ClientConfig(name, useBinaryProtocol, useConsistencyHash,
					connectionPoolSize, operationTimeout, hosts);
			clients.add(client);
		}
	}
	
	String getDefaultClient() {
		return defaultClient;
	}
	
	List<ClientConfig> getClients() {
		return clients;
	}
}
