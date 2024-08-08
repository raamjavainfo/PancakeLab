package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {
    private PancakeService pancakeService = new PancakeService();
    private Order          order          = null;

    private final static String DARK_CHOCOLATE_PANCAKE_DESCRIPTION           = "Delicious pancake with dark chocolate!";
    private final static String MILK_CHOCOLATE_PANCAKE_DESCRIPTION           = "Delicious pancake with milk chocolate!";
    private final static String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious pancake with milk chocolate, hazelnuts!";

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        order = pancakeService.createOrder(10, 20);

        assertEquals(10, order.getBuilding());
        assertEquals(20, order.getRoom());

        // verify

        // tear down
    }
    @Test
    @org.junit.jupiter.api.Order(80)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithInCorrectData_Test(){
        order = pancakeService.createOrder(-10, 20);

        assertEquals(null , order);
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);

        // exercise
        addPancakes();

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(21)
    public void GivenOrderExists_WhenAddingPancakesWithIncorrectInput_ThenNumberOfPancakesAdded_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);

        // exercise
        pancakeService.addDarkChocolatePancake(order.getId(), -1);
        pancakeService.addMilkChocolatePancake(order.getId(), -10);
        pancakeService.addMilkChocolateHazelnutsPancake(order.getId(), -30);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);
        addPancakes();

        // exercise
        pancakeService.removePancakes(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 2);
        pancakeService.removePancakes(MILK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 3);
        pancakeService.removePancakes(MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, order.getId(), 1);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                             MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);
        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        Set<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
        assertTrue(completedOrdersOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertTrue(preparedOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);


        List<String> pancakesToDeliver = pancakeService.viewOrder(order.getId());

        // exercise
        Object[] deliveredOrder = pancakeService.deliverOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);
        //assertEquals(order.getId(), ((Order) deliveredOrder[0]).getId());
        //assertEquals(pancakesToDeliver, (List<String>) deliveredOrder[1]);

        // tear down
        order = null;
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        order = pancakeService.createOrder(10, 20);
        addPancakes();

        // exercise
        pancakeService.cancelOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(), ordersPancakes);

        // tear down
    }

    private void addPancakes() {
        pancakeService.addDarkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolateHazelnutsPancake(order.getId(), 3);
    }

    @Test
    @org.junit.jupiter.api.Order(90)
    public void GivenOrdersDoesNotExist_WhenCreatingMultipleOrders_ThenOrdersCreatedWithCorrectData_Test() {
        // setup
        for(int i = 1; i <= 5; i++){
            if(i ==1 ) {
                pancakeService.addDarkChocolatePancake(pancakeService.createOrder(i, i + 20).getId(), 1);
            }
            else if(i ==2 ) {
                pancakeService.addMilkChocolatePancake(pancakeService.createOrder(i, i + 20).getId(), 1);
            }

          else if(i ==3 ) {
                pancakeService.addMilkChocolateHazelnutsPancake(pancakeService.createOrder(i, i + 20).getId(), 1);
            }
            else if(i ==4 ) {
                pancakeService.addDarkChocolateWhippedCreamHazelnutsPancake(pancakeService.createOrder(i, i + 20).getId(), 1);
            }
            else if(i ==5 ) {
                pancakeService.addDarkChocolateWhippedCreamPancake(pancakeService.createOrder(i, i + 20).getId(), 1);
            }

        }

        // exercise
        for(PancakeRecipe pancake : pancakeService.pancakeList()){
           //building number and room number
            //first order
            if(pancake instanceof DarkChocolatePancake && !(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake) && !(pancake instanceof DarkChocolateWhippedCreamPancake)){

                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(1, order.getBuilding());
                        assertEquals(21, order.getRoom());
                    }
                }
            }
            //Second
            if(pancake instanceof MilkChocolatePancake && !(pancake instanceof MilkChocolateHazelnutsPancake)){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(2, order.getBuilding());
                        assertEquals(22, order.getRoom());
                    }
                }
            }
            //Third
            if(pancake instanceof MilkChocolateHazelnutsPancake){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(3, order.getBuilding());
                        assertEquals(23, order.getRoom());
                    }
                }
            }
            //Fourth
            if(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(4, order.getBuilding());
                        assertEquals(24, order.getRoom());
                    }
                }
            }
            //Fifth
            if(pancake instanceof DarkChocolateWhippedCreamPancake && !(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake)){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(5, order.getBuilding());
                        assertEquals(25, order.getRoom());
                    }
                }
            }

        }

    }


    @Test
    @org.junit.jupiter.api.Order(91)
    public void GivenOrdersDoesNotExist_WhenCreatingMultipleOrdersParrallel_ThenOrdersCreatedWithCorrectNumberOfPancake_Test() throws InterruptedException {
        // setup

        Thread t1 = new Thread(
            ()->{
                int building = Thread.currentThread().getName().length();
                createOrderAndCreatePancakes(building, 5);
            });
        Thread t2 = new Thread(
                ()->{
                    int building = Thread.currentThread().getName().length();
                    createOrderAndCreatePancakes(building, 5);
                });
        t1.setName("Thread1");
        t1.start();
        t2.setName("ThreadThread");
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(10, pancakeService.pancakeList().size());
    }
    @Test
    @org.junit.jupiter.api.Order(92)
    public void GivenOrdersDoesNotExist_WhenCreatingMultipleOrdersParrallel_ThenOrdersCreatedWithCorrectData_Test() throws InterruptedException {
        // setup

        Thread t1 = new Thread(
                ()->{
                    int building = Thread.currentThread().getName().length();
                    createOrderAndCreateMilkPancakes(building, 5);
                });
        Thread t2 = new Thread(
                ()->{
                    int building = Thread.currentThread().getName().length();
                    createOrderAndCreateDarkPancakes(building, 5);
                });
        t1.setName("Thread1");
        t1.start();
        t2.setName("ThreadThread");
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(5, pancakeService.pancakeList().size());
        // exercise
        for(PancakeRecipe pancake : pancakeService.pancakeList()){
            //building number and room number
            //first order
            if(pancake instanceof DarkChocolatePancake && !(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake) && !(pancake instanceof DarkChocolateWhippedCreamPancake)){

                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(13, order.getBuilding());
                        assertEquals(33, order.getRoom());
                    }
                }
            }
            //Second
            if(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(14, order.getBuilding());
                        assertEquals(34, order.getRoom());
                    }
                }
            }
            //Third
            if(pancake instanceof DarkChocolateWhippedCreamPancake && !(pancake instanceof DarkChocolateWhippedCreamHazelnutsPancake)){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(15, order.getBuilding());
                        assertEquals(35, order.getRoom());
                    }
                }
            }
            //Fourth
            if(pancake instanceof MilkChocolatePancake && !(pancake instanceof MilkChocolateHazelnutsPancake)){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(8, order.getBuilding());
                        assertEquals(28, order.getRoom());
                    }
                }
            }
            //Fifth
            if(pancake instanceof MilkChocolateHazelnutsPancake){
                for(Order order: pancakeService.orderList()){
                    if(pancake.getOrderId().equals(order.getId())){
                        assertEquals(9, order.getBuilding());
                        assertEquals(29, order.getRoom());
                    }
                }
            }



        }

    }
    public void createOrderAndCreateMilkPancakes(int building, int count){
        for(int i = 1; i <= count; i++){
           if(i ==1 ) {
                pancakeService.addMilkChocolatePancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }

            else if(i ==2 ) {
                pancakeService.addMilkChocolateHazelnutsPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }

        }
    }

    public void createOrderAndCreateDarkPancakes(int building, int count){
        for(int i = 1; i <= count; i++){
            if(i ==1 ) {
                pancakeService.addDarkChocolatePancake(pancakeService.createOrder(building+i, building + i + 20).getId(), 1);
            }
            else if(i == 2) {
                pancakeService.addDarkChocolateWhippedCreamHazelnutsPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }
            else if(i == 3 ) {
                pancakeService.addDarkChocolateWhippedCreamPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }

        }
    }

    public void createOrderAndCreatePancakes(int building, int count){
        for(int i = 1; i <= count; i++){
            if(i ==1 ) {
                pancakeService.addDarkChocolatePancake(pancakeService.createOrder(building+i, building + i + 20).getId(), 1);
            }
            else if(i == 2) {
                pancakeService.addDarkChocolateWhippedCreamHazelnutsPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }
            else if(i == 3 ) {
                pancakeService.addDarkChocolateWhippedCreamPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }
            else  if(i ==4 ) {
                pancakeService.addMilkChocolatePancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }

            else if(i == 5) {
                pancakeService.addMilkChocolateHazelnutsPancake(pancakeService.createOrder(building + i, building + i + 20).getId(), 1);
            }

        }
    }
}
