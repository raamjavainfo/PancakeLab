package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;

public class DarkChocolateWhippedCreamHazelnutsPancake extends DarkChocolateWhippedCreamPancake {
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
    public List<String> ingredients() {
        if(validateIngredients(List.of("dark chocolate", "whipped cream","hazelnuts"))) {
            return List.of("dark chocolate", "whipped cream", "hazelnuts");
        }
        return null;
    }
}
