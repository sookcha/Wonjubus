package com.sookcha.wonjubus;

/**
 * Created by sookcha on 14. 11. 21..
 */
public class Favorites {

    public int id;
    public String name;
    public Integer number;
    public String location;
    public String locationLAT;
    public String locationLNG;

    public Favorites(){}

    public Favorites(String name, Integer number, String location, String locationLAT, String locationLNG) {
        super();
        this.name = name;
        this.number = number;
        this.location = location;
        this.locationLAT = locationLAT;
        this.locationLNG = locationLNG;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", number=" + number;

    }

}
