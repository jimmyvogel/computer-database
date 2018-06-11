package com.excilys.cdb.ui;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class InstanceWebService {

	private static final String PROTOCOLE = "http://";
	private static final String DOMAINE = "localhost";
	private static final String PORT = ":8090";
	private static final String URL_WEB_SERVICES = "/webservices";

	private static final String URI = PROTOCOLE + DOMAINE + PORT + URL_WEB_SERVICES;
	private static final String USER = "admin";
	private static final String PASSWORD = "admin@123";

	protected WebTarget webTarget;
	protected Client client;
	
	public InstanceWebService(String ressources) {
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive()
				.credentials(USER, PASSWORD).build();
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(feature);
		client = ClientBuilder.newClient(clientConfig);
		webTarget = client.target(URI + "/" + ressources);
	}
}
