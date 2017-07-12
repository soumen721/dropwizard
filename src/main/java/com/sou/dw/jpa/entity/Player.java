package com.sou.dw.jpa.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@NamedQueries({
        @NamedQuery(name = "Player.findByName", query = "SELECT p FROM Player p WHERE p.name = :name ORDER BY p.id")
})
@Table(name = "PLAYER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonProperty("scoreList")
	@JsonManagedReference
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Score> scoreList = new HashSet<Score>();;

    @NotNull
    @Size(min = 3, max = 45)
    @Column(name = "NAME", length = 45)
    private String name;

    @NotNull
    @Size(min = 3, max = 45)
    @Column(name = "LOGIN", length = 45)
    private String login;

    @JsonIgnore
    public Set<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(Set<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
