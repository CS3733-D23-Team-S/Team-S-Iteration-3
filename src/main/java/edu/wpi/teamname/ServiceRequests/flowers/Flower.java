package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.DAOs.IDataPack;
import lombok.Getter;
import lombok.Setter;

/** Flower: really a bouquet in practice but just called flower for simplicity */
public class Flower implements IDataPack {
  @Getter @Setter private int ID;
  @Getter @Setter private String name;
  @Getter @Setter private Size size;
  @Getter @Setter private double price;
  @Getter @Setter private int quantity;
  @Getter @Setter private String message;
  @Getter @Setter boolean isSoldOut;
  @Getter @Setter private String description;
  @Getter @Setter private String image;

  public Flower(
      int ID,
      String name,
      Size size,
      double price,
      int quantity,
      String message,
      boolean isSoldOut,
      String description,
      String image) {
    this.ID = ID;
    this.name = name;
    this.size = size;
    this.price = price;
    this.quantity = quantity;
    this.message = message;
    this.isSoldOut = isSoldOut;
    this.description = description;
    this.image = image;
  }

  public boolean getIsSoldOut() {
    return this.isSoldOut;
  }

  @Override
  public String toString() {
    return name;
  }

  public String toCSVString() {
    return ID
        + ","
        + name
        + ","
        + size
        + ","
        + price
        + ","
        + quantity
        + ","
        + isSoldOut
        + ","
        + description
        + ","
        + image;
  }
}
