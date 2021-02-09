//Bismillah Hirrahman Nirrahim

package com.toptal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
		  prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
    private ApplicationContext applicationContext;
	
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
    	/* 
    	 * https://www.baeldung.com/spring-security-create-new-custom-security-expression Section 5.3
    	 */
    	
    	CustomMethodSecurityExpressionHandler expressionHandler = 
    	          new CustomMethodSecurityExpressionHandler();
    	        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
    	        expressionHandler.setApplicationContext(applicationContext);
    	        return expressionHandler;
    	        
    	/*
        DefaultMethodSecurityExpressionHandler expressionHandler = 
          new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
        */
    }
    
}
