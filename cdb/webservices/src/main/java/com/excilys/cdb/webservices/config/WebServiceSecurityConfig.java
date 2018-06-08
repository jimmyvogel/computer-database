package com.excilys.cdb.webservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebServiceSecurityConfig extends WebSecurityConfigurerAdapter {

    public final static String AUTHORIZATION_HEADER = "authorization";

    //@Autowired
    //private TokenProvider tokenProvider;

	private UserDetailsService userDetailsService;

	public WebServiceSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        JWTFilter customFilter = new JWTFilter(this.tokenProvider);
//        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        // @formatter:off
        http.httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/**")
        .authenticated();
        // @formatter:on
        http.csrf().disable();
    }
    
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception        
//    {
//        return super.authenticationManagerBean();
//    }
}