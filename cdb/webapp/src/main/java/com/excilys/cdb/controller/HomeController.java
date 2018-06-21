package com.excilys.cdb.controller;

import java.security.Principal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.ressources.JspRessources;
import com.excilys.cdb.ressources.UrlRessources;
import com.excilys.cdb.service.IUserService;

@Controller
@RequestMapping("/")
public class HomeController {

	public static final String LOGIN_FORM = "login";
	public static final String LOGIN = "connect";
	public static final String LOGOUT = "logout";
	public static final String HOME = "index";
	public static final String INSCRIPTION_FORM = "inscription";
	public static final String INSCRIPTION = "signin";
	
	private IUserService serviceUser;

	public HomeController(IUserService serviceUser) {
		this.serviceUser = serviceUser;
	}
	
	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping(HOME)
	public String index(Model model, Principal principal) {
	    model.addAttribute(JspRessources.SUCCESS, "You are logged in as " + principal.getName());
		return UrlRessources.ACCUEIL;
	}
	
	/**
	 * Direction login.
	 * @return nom de la jsp
	 */
	@GetMapping(LOGIN_FORM)
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
	
	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@GetMapping(INSCRIPTION_FORM)
	public String inscription() {
		return UrlRessources.SIGNIN;
	}
	
	/**
	 * Direction accueil.
	 * @return nom de la jsp
	 */
	@PostMapping(INSCRIPTION)
	public ModelAndView signin(@RequestParam(JspRessources.FORM_SIGNIN_USERNAME) String username, 
			@RequestParam(JspRessources.FORM_SIGNIN_PASSWORD) String password,
			@RequestParam(JspRessources.FORM_SIGNIN_PASSWORD_VERIF) String passwordverif) {
		ModelAndView modelview;
		if (!password.equals(passwordverif)) {
			modelview = new ModelAndView(UrlRessources.SIGNIN);
			modelview.addObject(JspRessources.ERROR, "Password verification different.");
		} else {
			String encoded=new BCryptPasswordEncoder().encode(password);
			serviceUser.inscription(username, encoded);
			modelview = new ModelAndView(UrlRessources.LOGIN);
			modelview.addObject(JspRessources.ERROR, "Inscription done");
		}
		return modelview;
	}
	
}