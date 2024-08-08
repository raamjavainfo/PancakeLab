package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface PancakeRecipe {
    default String description()
    {
        return "Delicious pancake with ".concat(ingredients().stream().map(s->s.value).collect(Collectors.joining(", "))).concat("!");
        //.formatted(String.join(", ",

                        //collect(Collectors.joining()).toString()):""
       // : ingredients().stream().map((s)-> {

       //     return s.value;
        //}).collect(Collectors.joining()).toString()));
    }

    UUID getOrderId();
    void setOrderId(UUID orderId);
    List<Ingredient> ingredients();
}
