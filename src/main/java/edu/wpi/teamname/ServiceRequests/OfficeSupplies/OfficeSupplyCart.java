package edu.wpi.teamname.ServiceRequests.OfficeSupplies;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/** Cart class Cart is represented as a HashMap with Integer: id and Flower: flower */
public class OfficeSupplyCart {
    @Getter @Setter private int cartID;
    // @Getter @Setter private HashMap<Integer, Flower> cartItems;
    @Getter @Setter private ArrayList<OfficeSupply> cartItems;

    public OfficeSupplyCart(int cartID) {
        this.cartID = cartID;
        // cartItems = new HashMap<Integer, Flower>();
        cartItems = new ArrayList<OfficeSupply>();
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (OfficeSupply officeSupply : cartItems) {
            totalPrice += officeSupply.getPrice() * officeSupply.getQuantity();
        }

        return totalPrice;
    }

    public void addOfficeSupplyItem(OfficeSupply officeSupply) {
        cartItems.add(officeSupply);
    }

    @Override
    public String toString() {
        StringBuilder finale = new StringBuilder();
        boolean comma = false;

        for (OfficeSupply officeSupply : cartItems) {
            if (!comma) {
                finale.append(officeSupply.toString());
                comma = true;
            } else {
                finale.append(", ").append(officeSupply.toString());
            }
        }

        return finale.toString();
    }
}