package com.treep.frontend.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Getters, Setters, toString, equals, hashCode
@AllArgsConstructor // Constructeur avec tous les arguments
public class User {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("trips")
    private List<Trip> trips = new ArrayList<>();

    public User(Long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
