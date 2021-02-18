//Bismillah Hirrahman Nirrahim

package com.jogger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class MethodSecurityIntegrationTest {
	/*
	@Test
	@WithMockUser(username = "john", roles = { "ADMIN" })
	public void givenRoleViewer_whenCallGetUsername_thenReturnUsername() {
	    String userName = userRoleService.getUsername();
	    
	    assertEquals("john", userName);
	}
	*/
}
