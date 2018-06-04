package com.excilys.cdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.excilys.cdb.ressources.UrlRessources;

@Controller
@RequestMapping("/")
public class HomeController {

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("index")
	public String index() {
		return UrlRessources.ACCUEIL;
	}

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("")
	public String empty() {
		return UrlRessources.ACCUEIL;
	}
}