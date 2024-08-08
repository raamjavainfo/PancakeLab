package org.pancakelab.model.pancakes;

public enum Ingredient {


    DARK_CHOCOLATE("DC","dark chocolate"),
    WHIPPEDCREAM("WC" ,"whipped cream"),
    HAZELNUTS("HNUTS","hazelnuts"),
    MILKCHOCOLATE("MC","milk chocolate");

    public final String code ;
    public final String value;
    Ingredient(String code, String value) {
        this.code=code;
        this.value=value;
    }
}
