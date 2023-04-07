package edu.wpi.teamname.serviceRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import lombok.Getter;
import lombok.Setter;

public class Food {
  @Setter @Getter private int ID;
  @Setter @Getter private int name;
  @Setter @Getter private String type;
  @Setter @Getter private int prepTime;
  @Setter @Getter private String cuisine;
  @Setter @Getter private float price;
  @Setter @Getter private String description;
  @Setter @Getter private int quantity;
  @Setter @Getter private boolean isSoldOut;
  @Setter @Getter private FoodFilter filter;
  @Setter @Getter private Image image;
  @Setter @Getter private int calories;

  public Food(
      int ID,
      int name,
      String type,
      int prepTime,
      String cuisine,
      float price,
      String description,
      int quantity,
      boolean isSoldOut,
      FoodFilter filter,
      BufferedImage image) {
    this.ID = ID;
    this.name = name;
    this.type = type;
    this.prepTime = prepTime;
    this.cuisine = cuisine;
    this.price = price;
    this.description = description;
    this.quantity = quantity;
    this.isSoldOut = isSoldOut;
    this.filter = filter;
    this.image = image;
  }

  public void increaseOrdered() {
    // amountOfTimesFoodHasBeenOrdered++;
  }

  public boolean walletFriendly() {
    return this.price <= 15;
  }
}
