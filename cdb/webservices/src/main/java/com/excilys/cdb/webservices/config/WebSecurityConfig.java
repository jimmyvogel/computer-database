package com.excilys.cdb.webservices.config;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	private UserDetailsService userDetailsService;
//
//	public WebSecurityConfig(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	};
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
////		http.sessionManagement().maximumSessions(1);
////		http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER").and().authorizeRequests()
////				.antMatchers("/login**").permitAll().and().formLogin().loginPage("/login")
////				.loginProcessingUrl("/" + Action.LOGIN.getValue()).permitAll().and().logout().logoutSuccessUrl("/login")
////				.permitAll().and().csrf().disable();
//	}
}