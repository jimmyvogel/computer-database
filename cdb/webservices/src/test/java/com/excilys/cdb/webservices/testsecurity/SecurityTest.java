package com.excilys.cdb.webservices.testsecurity;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class SecurityTest {

	//@Autowired
	//private RestTemplate simpleRestTemplate;
	 
	private TestRestTemplate restTemplate;
	
	public void init() {
		//restTemplate = new TestRestTemplate(simpleRestTemplate);
	}
	
	@Test
	public void givenMemUsers_whenGetPingWithValidUser_thenOk() {
	    ResponseEntity<String> result 
	      = makeRestCallToGetPing("memuser", "pass");
	 
	    assertTrue(result.getStatusCodeValue()==200);
	    assertTrue(result.getBody().equals("OK"));
	}
	 
	@Test
	public void givenExternalUsers_whenGetPingWithValidUser_thenOK() {
	    ResponseEntity<String> result 
	      = makeRestCallToGetPing("externaluser", "pass");
	 
	    assertTrue(result.getStatusCodeValue()==200);
	    assertTrue(result.getBody().equals("OK"));
	}
	 
	@Test
	public void givenAuthProviders_whenGetPingWithNoCred_then401() {
	    ResponseEntity<String> result = makeRestCallToGetPing();
	 
	    assertTrue(result.getStatusCodeValue()==401);
	}
	 
	@Test
	public void givenAuthProviders_whenGetPingWithBadCred_then401() {
	    ResponseEntity<String> result 
	      = makeRestCallToGetPing("user", "bad_password");
	 
	    assertTrue(result.getStatusCodeValue()==401);
	}
	 
	private ResponseEntity<String> 
	  makeRestCallToGetPing(String username, String password) {
	    return restTemplate.withBasicAuth(username, password)
	      .getForEntity("/api/ping", String.class, Collections.emptyMap());
	}
	 
	private ResponseEntity<String> makeRestCallToGetPing() {
	    return restTemplate
	      .getForEntity("/api/ping", String.class, Collections.emptyMap());
	}

}
