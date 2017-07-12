package com.example.dw.jpa.resource;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.dw.jpa.service.UserService;
import com.example.rest.basicauth.User;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserService userService;

    @Inject
    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @POST
    @RolesAllowed("ADMIN")
    public User addPlayer(@Valid User user) {
    	System.out.println("Requesting User Id : "+ user.getRoles());
    	userService.save(user);
        return user;
    }

    @GET
    @Path("/{id}")
    public User getPlayer(@PathParam("id") String id) {
    	User user = userService.findById(id);
    	return user;
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deletePlayer(@PathParam("id") String id) {
    	userService.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/find")
    @RolesAllowed("ADMIN")
    public List<User> getPlayerByName(@QueryParam("name") String name) {
        return userService.findUserByName(Optional.ofNullable(name));
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<User> getPlayerList() {
        return userService.findAll("name");
    }
}
