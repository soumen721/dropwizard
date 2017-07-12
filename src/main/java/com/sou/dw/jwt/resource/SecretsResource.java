package com.sou.dw.jwt.resource;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.jsonwebtoken.jjwtfun.service.SecretService;

@Path("/jwts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes("application/json")
public class SecretsResource{

	private SecretService secretService;

	@Inject
    public SecretsResource(final SecretService secretService) {
		this.secretService = secretService;
    }

	@GET
	@Path("/static-builder")
    public Map<String, String> getSecrets() {
        return secretService.getSecrets();
    }

	@GET
	@Path("/refresh-builder")
    public Map<String, String> refreshSecrets() {
        return secretService.refreshSecrets();
    }

	@POST
	@Path("/set-builder")
    public Map<String, String> setSecrets(Map<String, String> secrets) {
        secretService.setSecrets(secrets);
        return secretService.getSecrets();
    }
}
