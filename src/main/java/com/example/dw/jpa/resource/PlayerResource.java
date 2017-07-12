package com.example.dw.jpa.resource;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import javax.ws.rs.core.Response.Status;

import com.example.dw.jpa.entity.Player;
import com.example.dw.jpa.entity.Score;
import com.example.dw.jpa.service.PlayerService;
import com.example.rest.basicauth.User;

import io.dropwizard.auth.Auth;

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    private final PlayerService playerService;

    @Inject
    public PlayerResource(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @POST
    @RolesAllowed("ADMIN")
    public Player addPlayer(@Valid Player player, @Auth User user) {
    	System.out.println("Requesting User Id : "+ user.getRoles());
    	
    	/*Player player1 = new Player();
    	player1.setLogin("ROLE");
    	player1.setName("Soumen");
    	player1.setVersion(1L);
    	
    	Set<Score> set = new HashSet<>();
    	Score score = new Score();
    	score.setPoints(1L);
    	score.setStage(1L);
    	score.setVersion(1L);
    	score.setPlayer(player1);
    	set.add(score);
    	score = new Score();
    	score.setPoints(2L);
    	score.setStage(2L);
    	score.setVersion(2L);
    	score.setPlayer(player1);
    	set.add(score);
    	player1.setScoreList(set);*/
    	
        playerService.save(player);
        return player;
    }

    @GET
    @Path("/{id}")
    public Response getPlayer(@PathParam("id") Long id) {
    	Player player = playerService.findById(id);
    	if (player != null)
            return Response.ok(player).build();
        else
            return Response.status(Status.NOT_FOUND).build();

    }

    @DELETE
    @Path("/{id}")
    public Response deletePlayer(@PathParam("id") Long id) {
        playerService.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/find")
    public List<Player> getPlayerByName(@QueryParam("name") String name) {
        return playerService.findPlayerByName(Optional.ofNullable(name));
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<Player> getPlayerList() {
        return playerService.findAll("id");
    }
    
    @DELETE
    @RolesAllowed("ADMIN")
    public Response removeAllPlayer() throws Exception {
        playerService.removeAll();
        return Response.noContent().build();
    }
    
}
