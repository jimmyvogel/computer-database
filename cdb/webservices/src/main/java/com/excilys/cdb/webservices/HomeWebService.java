package com.excilys.cdb.webservices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class HomeWebService {

	@GetMapping("/ping")
	public ResponseEntity<Integer> getPing() {
		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	}

}
