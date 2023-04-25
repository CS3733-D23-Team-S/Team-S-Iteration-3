package edu.wpi.teamname;

import static org.junit.Assert.assertEquals;

import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyCart;
import org.junit.jupiter.api.Test;

public class OfficeSupplyTest {
  OfficeSupply os = new OfficeSupply(10000, "a name", 10.0, "me", 1, false, "none");

  @Test
  public void testOffice1() {
    assertEquals("a name", os.toString());
  }

  @Test
  public void testCart() {
    OfficeSupplyCart osc = new OfficeSupplyCart(1000);
    osc.getCartItems().add(os);
    assertEquals(10, osc.getTotalPrice(), .003);
  }
}
