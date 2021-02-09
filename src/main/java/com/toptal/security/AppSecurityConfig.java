// Bismillah Hirrahman Nirrahim
// https://www.youtube.com/watch?v=fjkelzWNSuA

package com.toptal.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@EnableOAuth2Sso
@Configuration
@EnableWebSecurity
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
	
	/* Uncomment this to enable Spring Security that takes username as password
	 * from a database, which is stored using BCrypt. The user/pass are 
	 * stored in DB manually (or through custom method). The following is only
	 * enabling the function.
	 * 
	 * BCrypt is a hashing method. This way, password is stored as a hash and not
	 * plaintext inside db
	 */
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new MyUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		// Data Access Object: DAO
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		//provider.setUserDetailsService(userDetailsService);
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Set your configuration on the auth object
		auth.authenticationProvider(authProvider());
		/*
		// What is the type of auth that you need?
		auth.inMemoryAuthentication()
		.withUser("blah")
		.password("blah")
		.roles("USER");
		*/
	}
	
	/* Enable the following block if you want to create a webapp
	 * that has a custom login and logout pages. These pages
	 * are not implemented in this project since this project is not asking
	 * for a webapp. 
	 * 
	 * Inside src/main/resources/webapp you will need
	 * login.jsp : a simple form with username and password
	 * home.jsp : a simple homepage saying welcome, without a logout link
	 * logout.jsp
	 * 403.jsp : for access denied, and make a @GetMapping error403() method in 
	 * Controller 
	 * For more info, watch: https://www.youtube.com/watch?v=fjkelzWNSuA
	 * from 46m40s
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll()
			.and()
			.logout().permitAll();
		*/
		/*
		http.authorizeRequests()
        //.antMatchers("/securityNone").permitAll()
		.antMatchers("/jogs/delete/**").hasAuthority("DELETE_RECORD")
		.antMatchers("/jogs/**").hasAuthority("READ_RECORD")
		.antMatchers("/jogs/add/**").hasAuthority("WRITE_RECORD")
		.antMatchers("/jogs/update/**").hasAuthority("UPDATE_RECORD")
		.antMatchers("/users/delete/**").hasAuthority("DELETE_USER")
		.antMatchers("/users/**").hasAuthority("READ_USER")
		.antMatchers("/users/add/**").hasAuthority("WRITE_USER")
		.antMatchers("/users/update/**").hasAuthority("UPDATE_USER")
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint);
		
		*/
		http.authorizeRequests()
		.antMatchers("/privileges/**").hasAuthority("ADMIN")
		.antMatchers("/privilege").hasAuthority("ADMIN")
		.antMatchers("/jogs/").hasAnyAuthority("ADMIN", "USER")
		.antMatchers("/jog").hasAnyAuthority("ADMIN", "USER")
		.antMatchers("/organizations/**").hasAnyAuthority("ADMIN")
		.antMatchers("/organization").hasAnyAuthority("ADMIN")
		.antMatchers("/users/**").hasAnyAuthority("ADMIN", "MANAGER")
		.antMatchers("/user").hasAnyAuthority("MANAGER", "ADMIN")
		.anyRequest().authenticated()
		.and()
		.httpBasic()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.csrf().disable();
		 /*
		http
			.csrf().disable()
			.authorizeRequests().antMatchers("/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			.logout().invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/logout-success").permitAll();
		 */
		
		
	}
	
		
	

	

}
