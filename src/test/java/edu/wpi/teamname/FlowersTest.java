package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamname.ServiceRequests.flowers.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FlowersTest {
  FlowerDAOImpl fDAOI = new FlowerDAOImpl();
  FlowerDeliveryDAOImpl FDDAOI = new FlowerDeliveryDAOImpl();
  Flower flower1;
  FlowerDelivery FD;

  @Test
  public void testNewFlower() {
    flower1 =
        new Flower(1000, "test flower", Size.LARGE, 10.0, 1, "Hello", false, "Flower", "image");

    fDAOI.add(flower1);

    assertEquals(fDAOI.get(1000), flower1);

    List<Flower> sizes = new ArrayList<>();
    for (Flower flower : fDAOI.getAll()) {
      if (flower.getSize().toString().equals("small")) sizes.add(flower);
    }

    assertEquals(sizes, fDAOI.getListSize("small"));
  }

  @Test
  public void testFlowerDelivery() {
    FD =
        new FlowerDelivery(1000, "acarrt", null, null, "a room", "me", "ur mom", "Complete", 10.01);

    FDDAOI.add(FD);

    assertEquals(FD, FDDAOI.get(1000));
  }

  @Test
  public void testCart() {
    Cart cart = new Cart(1000);
    Flower flower2 =
        new Flower(1001, "flower2", Size.SMALL, 20.0, 3, "waaaaaa", false, "Flower2", "image");

    cart.addFlowerItem(flower1);
    cart.addFlowerItem(flower2);

    assertEquals(cart.getTotalPrice(), 70);
  }
}
