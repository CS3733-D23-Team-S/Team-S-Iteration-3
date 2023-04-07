package edu.wpi.teamname.serviceRequest;

import lombok.Getter;
import lombok.Setter;

public class FoodFilter {
  @Setter @Getter private boolean isVegetarian;
  @Setter @Getter private boolean isGlutenFree;
  @Setter @Getter private boolean isAmerican;
  @Setter @Getter private boolean isMexican;
  @Setter @Getter private boolean isIndian;
  @Setter @Getter private boolean isItalian;

  public FoodFilter(
      boolean isVegetarian,
      boolean isGlutenFree,
      boolean isAmerican,
      boolean isMexican,
      boolean isIndian,
      boolean isItalian) {
    this.isVegetarian = isVegetarian;
    this.isGlutenFree = isGlutenFree;
    this.isAmerican = isAmerican;
    this.isMexican = isMexican;
    this.isIndian = isIndian;
    this.isItalian = isItalian;
  }
}
