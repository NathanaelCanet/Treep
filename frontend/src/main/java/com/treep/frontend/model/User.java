package com.treep.frontend.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // Constructeur sans argument
@AllArgsConstructor // Constructeur avec tous les arguments
public class User {

    private Long id;
    private String login;
    private String password;
    private Role role;
    private List<Trip> trips = new ArrayList<>();

    // Constructeur avec id, login, password, role (sans trips)
    public User(Long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
