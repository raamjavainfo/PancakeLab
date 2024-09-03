package org.pancakelab.model.pancakes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface PancakeRecipe {
    default String description()
    {
        return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
    }

    default Boolean validateIngredients(List<String> list){
        Boolean returnValue = false;
        for (String ingredient:list) {
            List<String> ingredientList = Arrays.stream(Ingredient.values()).map(item ->item.getName()).toList();
            if(ingredientList.contains(ingredient)){
                returnValue = true;
            }else {
                return false;
            }
        }
        return returnValue;
    }

    UUID getOrderId();
    void setOrderId(UUID orderId);
    List<String> ingredients();
}
