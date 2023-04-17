package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.IDataPack;
import edu.wpi.teamname.ServiceRequests.ISRDAO;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SRCart {


    // @Getter @Setter private HashMap<Integer, Flower> cartItems;
    @Getter @Setter private HashMap<IDataPack, Integer> cartItems;

    public SRCart() {

        // cartItems = new HashMap<Integer, Flower>();
       cartItems = new HashMap<>();
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (IDataPack item : cartItems.keySet()) {
            totalPrice += item.getPrice() * cartItems.get(item);
        }

        return totalPrice;
    }

    public void addItem(IDataPack dataItem, int quantity) {
        cartItems.put(dataItem, quantity);
    }

    public void deleteItem(IDataPack dataItem){
        cartItems.remove(dataItem);
    }


    @Override
    public String toString() {
        return "SRCart{" +
                "cartItems=" + cartItems +
                '}';
    }
}

