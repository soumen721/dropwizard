package com.sou.rest.basicauth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sou.dw.jpa.service.UserService;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class AppAuthenticator implements Authenticator<BasicCredentials, User> {
   
	private UserService userService;

    @Inject
    public AppAuthenticator(final UserService userService) {
        this.userService = userService;
    }
    
	private static Map<String, Set<String>> VALID_USERS1 = ImmutableMap.of(
        "guest", ImmutableSet.of(),
        "user", ImmutableSet.of("USER"),
        "admin", ImmutableSet.of("ADMIN", "USER")
    );

	private  Map<String, User> VALID_USERS = new HashMap<String, User>();
	
	
	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
		Set<String> roles = new HashSet<String>();
		roles.add("ADMIN");
		roles.add("GUEST");
    	VALID_USERS.put("admin", new User("admin", "password", roles) );
    	
    	List<User> user = userService.findUserByName(Optional.ofNullable(credentials.getUsername()));
    	for (User user2 : user) {
			VALID_USERS.put(user2.getName(), user2);
		}
    	
    	if (VALID_USERS.containsKey(credentials.getUsername()) && ((User)VALID_USERS.get(credentials.getUsername())).getPassword().equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername(), VALID_USERS.get(credentials.getUsername()).getRoles()));
        }
    	
        /*if (VALID_USERS.containsKey(credentials.getUsername()) && "password".equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername(), VALID_USERS.get(credentials.getUsername())));
        }*/
        return Optional.empty();
    }
}
