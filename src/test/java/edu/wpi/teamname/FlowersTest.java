package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.ServiceRequests.flowers.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FlowersTest {
  Flower flower1;
  FlowerDelivery FD;
  static DataBaseRepository dbr = DataBaseRepository.getInstance();

  @BeforeAll
  public static void start() {
    dbr.getUserDAO().loadRemote("k");
  }

  /*@Test
  public void testNewFlower() {
    flower1 =
        new Flower(1000, "test flower", Size.LARGE, 10.0, 1, "Hello", false, "Flower", "image");

    dbr.getFlowerDAO().add(flower1);
    System.out.println("Hereerere");

    assertEquals(dbr.getFlowerDAO().get(1000), flower1);

    List<Flower> sizes = new ArrayList<>();

    for (Flower flower : dbr.getFlowerDAO().getAll()) {
      if (flower.getSize().toString().equals("small")) sizes.add(flower);
    }

    assertEquals(sizes, dbr.getFlowerDAO().getListOfSize("small"));

    dbr.getFlowerDAO().delete(1000);
  }*/

  @Test
  public void testFlowerDelivery() {
    FD =
        new FlowerDelivery(
            1000,
            "acarrt",
            Date.valueOf(LocalDate.of(2022, 5, 13)),
            Time.valueOf(LocalTime.of(14, 45)),
            "a room",
            "me",
            "ur mom",
            "Complete",
            10.01,
            "n");

    dbr.getFlowerDeliveryDAO().add(FD);

    assertEquals(FD, dbr.getFlowerDeliveryDAO().get(1000));
  }

  @Test
  public void testCart() {
    Cart cart = new Cart(1000);
    Flower flower2 =
        new Flower(1001, "flower2", Size.SMALL, 20.0, 3, "waaaaaa", false, "Flower2", "image");
    flower1 =
        new Flower(1000, "test flower", Size.LARGE, 10.0, 1, "Hello", false, "Flower", "image");
    cart.addFlowerItem(flower1);
    cart.addFlowerItem(flower2);

    assertEquals(cart.getTotalPrice(), 70);
  }

  @AfterAll
  public static void closeDB() throws SQLException {
    dbConnection.getInstance().getConnection().close();
  }
}
