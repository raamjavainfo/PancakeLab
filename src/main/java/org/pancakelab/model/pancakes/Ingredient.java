package org.pancakelab.model.pancakes;

public enum Ingredient {

    MC("milk chocolate"),
    HN("hazelnuts"),
    WC("whipped cream"),
    DC("dark chocolate");

    private String name;

    Ingredient(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
