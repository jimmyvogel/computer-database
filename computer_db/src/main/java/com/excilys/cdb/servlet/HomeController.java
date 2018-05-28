package com.excilys.cdb.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("index")
	public String index() {
		return "accueil";
	}

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("")
	public String empty() {
		return "accueil";
	}
}