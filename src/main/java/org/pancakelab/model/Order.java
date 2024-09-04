package org.pancakelab.model;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private final UUID id;
    private final Integer building;
    private final Integer room;

    public Order(Integer building, Integer room) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.room = room;
    }

    public UUID getId() {
        return id;
    }

    public Integer getBuilding() {
        return building;
    }

    public Integer getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
