package com.esprit.authUser.models;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    private Long id;
    private String name;
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return name;
    }

    public void setRole(String name) {
        this.name = name;
    }

    
}