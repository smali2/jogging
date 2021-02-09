// Bismillah Hirrahman Nirrahim

package com.toptal.security;

import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.server.core.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler 
extends DefaultMethodSecurityExpressionHandler {
	
	  private AuthenticationTrustResolver trustResolver = 
	    new AuthenticationTrustResolverImpl();
	  
	  private ApplicationContext applicationContext;
	  
	  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
	    Authentication authentication, MethodInvocation invocation) {
	      CustomMethodSecurityExpressionRoot root = 
	        new CustomMethodSecurityExpressionRoot(authentication);
	      root.setPermissionEvaluator(getPermissionEvaluator());
	      root.setTrustResolver(this.trustResolver);
	      root.setRoleHierarchy(getRoleHierarchy());
	      return root;
	  }
	  
	  @Override
	  public void setApplicationContext(ApplicationContext applicationContext) {
	      super.setApplicationContext(applicationContext);
	      this.applicationContext = applicationContext;
	  }
  
}
