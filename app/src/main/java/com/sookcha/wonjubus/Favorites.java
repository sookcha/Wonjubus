package com.sookcha.wonjubus;

/**
 * Created by sookcha on 14. 11. 21..
 */
public class Favorites {

    public int id;
    public String name;
    public Integer number;

    public Favorites(){}

    public Favorites(String name, Integer number) {
        super();
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", number=" + number;

    }

}
