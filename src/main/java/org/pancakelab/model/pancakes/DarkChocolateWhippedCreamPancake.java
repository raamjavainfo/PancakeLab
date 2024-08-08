package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;

public class DarkChocolateWhippedCreamPancake extends DarkChocolatePancake {
    private UUID orderId;

    @Override
    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<Ingredient> ingredients() {
        return List.of(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPEDCREAM);
    }
}
