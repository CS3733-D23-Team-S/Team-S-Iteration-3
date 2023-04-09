package edu.wpi.teamname.ServiceRequests.flowers;

import lombok.Getter;
import lombok.Setter;

/** Flower: really a bouquet in practice but just called flower for simplicity */
public class Flower {
  @Getter @Setter private int ID;
  @Getter @Setter private String name;
  @Getter @Setter private String size;
  @Getter @Setter private double price;
  @Getter @Setter private int quantity;

  private boolean isSoldOut;
  @Getter @Setter private String description;
  @Getter @Setter private String image;

  public Flower(
      int ID,
      String name,
      String size,
      double price,
      int quantity,
      boolean isSoldOut,
      String description,
      String image) {
    this.ID = ID;
    this.name = name;
    this.size = size;
    this.price = price;
    this.quantity = quantity;
    this.isSoldOut = isSoldOut;
    this.description = description;
    this.image = image;
  }

  public boolean getIsSoldOut() {
    return this.isSoldOut;
  }

  @Override
  public String toString() {
    return "Flower{"
        + "ID="
        + ID
        + ", name='"
        + name
        + '\''
        + ", size='"
        + size
        + '\''
        + ", price="
        + price
        + ", quantity="
        + quantity
        + ", isSoldOut="
        + isSoldOut
        + ", description='"
        + description
        + '\''
        + ", image='"
        + image
        + '\''
        + '}';
  }
}
