package edu.wpi.teamname.ServiceRequests.OfficeSupplies;

import edu.wpi.teamname.DAOs.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class OfficeSupply implements IDataPack {

  @Getter private final int officesupplyid;
  @Getter @Setter private String name;
  @Getter @Setter private double price;
  @Getter @Setter private String description;
  @Getter @Setter private int quantity;
  @Getter @Setter private boolean isSoldOut;
  @Getter @Setter private String image;

  public OfficeSupply(int id, String n, double p, String d, int q, boolean so, String i) {
    this.officesupplyid = id;
    this.name = n;
    this.price = p;
    this.description = d;
    this.quantity = q;
    this.isSoldOut = so;
    this.image = i;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public String toCSVString() {
    String finale;

    finale =
        officesupplyid
            + ","
            + name
            + ","
            + price
            + ","
            + description
            + ","
            + quantity
            + ","
            + isSoldOut
            + ","
            + image
            + ",";

    return finale;
  }
}
