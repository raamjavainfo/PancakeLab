package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private final List<Order>         orders          = new ArrayList<>();
    private final Set<UUID>           completedOrders = new HashSet<>();
    private final Set<UUID>           preparedOrders  = new HashSet<>();
    private final List<PancakeRecipe> pancakes        = new ArrayList<>();

    public Order createOrder(int building, int room) {
        if( !Validation.validatePositveNumber(building) || !Validation.validatePositveNumber(room)){
            OrderLog.logValidation();
            return null;
        }

        Order order = new Order(building, room);
        synchronized (orders) {
            orders.add(order);
        }
        return order;
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        if( !Validation.validatePositveNumber(count)){
            System.out.println("Invalid input value.");
            return;
        }
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            for (int i = 0; i < count; ++i) {
                addPancake(new DarkChocolatePancake(), order);
            }
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        if( !Validation.validatePositveNumber(count)){
            OrderLog.logValidation();
            return;
        }
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            for (int i = 0; i < count; ++i) {
                addPancake(new DarkChocolateWhippedCreamPancake(), order);
            }
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        if( !Validation.validatePositveNumber(count)){
            OrderLog.logValidation();
            return;
        }
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            for (int i = 0; i < count; ++i) {
                addPancake(new DarkChocolateWhippedCreamHazelnutsPancake(), order);
            }
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        if( !Validation.validatePositveNumber(count)){
            OrderLog.logValidation();
            return;
        }
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            for (int i = 0; i < count; ++i) {
                addPancake(new MilkChocolatePancake(), order);
            }
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            for (int i = 0; i < count; ++i) {
                addPancake(new MilkChocolateHazelnutsPancake(), order);
            }
        }
    }

    public List<String> viewOrder(UUID orderId) {
        synchronized (pancakes) {
            return pancakes.stream()
                    .filter(pancake -> pancake.getOrderId().equals(orderId))
                    .map(PancakeRecipe::description).toList();
        }
    }

    private void addPancake(PancakeRecipe pancake, Order order) {
        synchronized (pancakes) {
            pancake.setOrderId(order.getId());
            pancakes.add(pancake);
            OrderLog.logAddPancake(order, pancake.description(), pancakes);
        }
    }

    public void removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        synchronized (pancakes) {
            pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId) &&
                    pancake.description().equals(description) &&
                    removedCount.getAndIncrement() < count);
        }
        synchronized (orders) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            OrderLog.logRemovePancakes(order, description, removedCount.get(), pancakes);
        }
    }

    public void cancelOrder(UUID orderId) {
        synchronized (this) {
            Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
            OrderLog.logCancelOrder(order, this.pancakes);

            pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
            orders.removeIf(o -> o.getId().equals(orderId));
            completedOrders.removeIf(u -> u.equals(orderId));
            preparedOrders.removeIf(u -> u.equals(orderId));

            OrderLog.logCancelOrder(order, pancakes);
        }
    }

    public void completeOrder(UUID orderId) {
        synchronized (completedOrders) {
            completedOrders.add(orderId);
        }
    }

    public Set<UUID> listCompletedOrders() {
        synchronized (completedOrders) {
            return new HashSet<>(completedOrders);
        }
    }

    public void prepareOrder(UUID orderId) {
        synchronized (preparedOrders) {
            preparedOrders.add(orderId);
            synchronized (completedOrders) {
                completedOrders.remove(orderId);
            }
        }
    }

    public Set<UUID> listPreparedOrders() {
        synchronized (preparedOrders) {
            return new HashSet<>(preparedOrders);
        }
    }

    public Object[] deliverOrder(UUID orderId) {
        synchronized (preparedOrders) {
            if (!preparedOrders.contains(orderId)) return null;

            synchronized (this) {
                Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElseThrow();
                List<String> pancakesToDeliver = viewOrder(orderId);
                OrderLog.logDeliverOrder(order, this.pancakes);

                pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
                orders.removeIf(o -> o.getId().equals(orderId));
                preparedOrders.removeIf(u -> u.equals(orderId));

                return new Object[]{order, };
            }
        }
    }

    //added method for testing purpose
    public synchronized List<Order> orderList(){
        return orders;
    }

    //added method for testing purpose
    public synchronized List<PancakeRecipe> pancakeList(){
        return pancakes;
    }
}
