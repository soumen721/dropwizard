package com.example.dw.jwt.resource;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.nimbusds.jwt.JWTClaimsSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jjwtfun.model.JwtResponse;
import io.jsonwebtoken.jjwtfun.service.SecretService;

@Path("/jwt")
@Produces(MediaType.APPLICATION_JSON)
public class StaticJWTResource {

	private SecretService secretService;

	@Inject
    public StaticJWTResource(final SecretService secretService) {
		this.secretService = secretService;
    }

	@GET
	@Path("/get-token")
	public JwtResponse fixedBuilder() throws UnsupportedEncodingException {
		String jws = Jwts.builder()
				.setIssuer("Soumen")
				.setSubject("msilverman")
				.claim("name", "Soumen Choudhury")
				.claim("Role", "ADMIN")
				.claim("Role", "USER")
				.claim("scope", "admins")
				.setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L))) // Fri Jun 24 2016 15:33:42  GMT-0400 (EDT)
				.setExpiration(Date.from(Instant.ofEpochSecond(4622470422L))) // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
				.signWith(SignatureAlgorithm.HS256, secretService.getHS256SecretBytes()).compact();

		return new JwtResponse(jws);
	}

	@GET
	@Path(value = "/parser-token")
	public JwtResponse parser(@QueryParam(value = "jwt") String jwt) throws UnsupportedEncodingException {

		Jws<Claims> jws = Jwts.parser().setSigningKeyResolver(secretService.getSigningKeyResolver())
				.parseClaimsJws(jwt);

		return new JwtResponse(jws);
	}

	@Path(value = "/parser-enforce")
	@GET
	public JwtResponse parserEnforce(@QueryParam(value = "jwt") String jwt) throws UnsupportedEncodingException {
		Jws<Claims> jws = Jwts.parser().requireIssuer("Stormpath").require("hasMotorcycle", true)
				.setSigningKeyResolver(secretService.getSigningKeyResolver()).parseClaimsJws(jwt);

		return new JwtResponse(jws);
	}
}
