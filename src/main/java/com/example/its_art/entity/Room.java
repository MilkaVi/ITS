package com.example.its_art.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String country;
    private boolean state;

    public Room(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Room(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Room() {
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void turn(){
        if(isState())
            setState(false);
        else
            setState(true);
    }

}