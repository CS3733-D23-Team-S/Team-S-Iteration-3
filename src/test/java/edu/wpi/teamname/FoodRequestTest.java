package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.teamname.ServiceRequests.FoodService.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class FoodRequestTest {

  Food f1 =
      new Food(
          21,
          "Burrito",
          "Mexican",
          12,
          "Mexican",
          21.0,
          "delicious",
          1,
          true,
          "test",
          250,
          "test",
          false,
          false,
          true,
          false,
          true,
          true,
          false,
          true,
          false);
  Food f2 =
      new Food(
          21,
          "Burrito",
          "Mexican",
          12,
          "Mexican",
          21.0,
          "delicious",
          1,
          true,
          "test",
          250,
          "test",
          false,
          false,
          true,
          false,
          true,
          true,
          false,
          true,
          false);

  ArrayList<Food> l1 = new ArrayList<>();

  FoodDeliveryDAOImp foodDeliveryDAO = new FoodDeliveryDAOImp();
  FoodDAOImpl foodDAO = new FoodDAOImpl();

  FoodDelivery foodDelivery1 = null;

  Food food1 = null;

  @Test
  public void isWalletFriendlyTest() {
    f1.isWalletFriendly();
    assertFalse(f1.isWalletFriendly());
  }

  @Test
  public void isQuickDeliveryTest() {
    f1.isQuickDelivery();
    assertTrue(f1.isQuickDelivery());
  }

  @Test
  public void isAmericanTest() {
    f1.isAmerican();
    assertFalse(f1.isAmerican());
  }

  @Test
  public void isIndianTest() {
    f1.isIndian();
    assertFalse(f1.isIndian());
  }

  @Test
  public void isMexicanTest() {
    f1.isMexican();
    assertTrue(f1.isMexican());
  }

  @Test
  public void isItalianTest() {
    f1.isItalian();
    assertFalse(f1.isItalian());
  }

  @Test
  public void isVegetarianTest() {
    f1.isVegetarian();
    assertTrue(f1.isVegetarian());
  }

  @Test
  public void isVeganTest() {
    f1.isVegan();
    assertTrue(f1.isVegan());
  }

  @Test
  public void isHalalTest() {
    f1.isHalal();
    assertFalse(f1.isHalal());
  }

  @Test
  public void isGlutenFreeTest() {
    f1.isGlutFree();
    assertTrue(f1.isGlutFree());
  }

  @Test
  public void isKosherTest() {
    f1.isKosher();
    assertFalse(f1.isKosher());
  }

  @Test
  public void addFoodTest() {
    foodDAO.getFoods().put(f1.getFoodID(), f1);
    foodDAO.add(f1);

    assertTrue(foodDAO.getFoods().containsKey(f1.getFoodID()));
  }

  @Test
  public void deleteFoodTest() {
    foodDAO.getFoods().put(f1.getFoodID(), f1);
    foodDAO.delete(f1.getFoodID());
    assertFalse(foodDAO.getFoods().containsKey(f1));
  }

  @Test
  public void addFoodItemTest() {
    OrderItem orderItem = new OrderItem(1);
    orderItem.addFoodItem(f1);
//    assertTrue(orderItem.getTheCart().containsKey(f1.getFoodID()));
  }
}
