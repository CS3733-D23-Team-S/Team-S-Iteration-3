package edu.wpi.teamname.ServiceRequests.flowers;

import lombok.Getter;
import lombok.Setter;

public class OrderItem {
    @Getter @Setter
    private int cartID;
    @Getter @Setter
    private Flower item;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private FlowerDAOImpl flowerDAO;

    public OrderItem(int cartID, Flower item, int quantity, FlowerDAOImpl flowerDAO) {
        this.cartID = cartID;
        this.item = item;
        this.quantity = quantity;
        this.flowerDAO = flowerDAO;
    }

    public double calculateCost() {
        return 0;
    }

}
