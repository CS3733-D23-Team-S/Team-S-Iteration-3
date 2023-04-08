package edu.wpi.teamname.ServiceRequests.flowers;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Cart class
 * Cart is represented as a HashMap with Integer: id and Flower: flower
 *
 */
public class OrderItem {
    @Getter @Setter
    private int cartID;
    @Getter @Setter
    private HashMap<Integer, Flower> cartItems;

    public OrderItem(int cartID) {
        this.cartID = cartID;
        cartItems = new HashMap<Integer, Flower>();
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Flower flower: cartItems.values()) {
            totalPrice += flower.getPrice();
        }

        return totalPrice;
    }

    public void addFlowerItem(Flower flower) {
        cartItems.put(flower.getID(), flower);
    }

    @Override
    public String toString() {
        StringBuilder finale = new StringBuilder();
        boolean comma = false;

        for (Flower flower: cartItems.values()) {
            if (!comma) {
                finale.append(flower.toString());
                comma = true;
            }
            finale.append(", ").append(flower.toString());
        }

        return finale.toString();
    }

}
