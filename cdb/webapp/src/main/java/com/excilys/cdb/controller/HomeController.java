package com.excilys.cdb.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.excilys.cdb.ressources.JspRessources;
import com.excilys.cdb.ressources.UrlRessources;

@Controller
@RequestMapping("/")
public class HomeController {

	public static final String LOGIN_FORM = "login";
	public static final String LOGIN = "connect";
	public static final String LOGOUT = "logout";
	public static final String HOME = "index";
	
	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("index")
	public String index(Model model, Principal principal) {
	    model.addAttribute(JspRessources.SUCCESS, "You are logged in as " + principal.getName());
		return UrlRessources.ACCUEIL;
	}
	
	/**
	 * Direction login.
	 * @return nom de la jsp
	 */
	@GetMapping("/" + LOGIN_FORM)
	public String index() {
		return UrlRessources.LOGIN;
	}

	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping("")
	public String empty(Model model, Principal principal) {
		model.addAttribute(JspRessources.SUCCESS, "You are logged in as " + principal.getName());
		return UrlRessources.ACCUEIL;
	}
}