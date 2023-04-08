package edu.wpi.teamname.ServiceRequests.flowers;

import java.util.List;

public interface FlowerDeliveryDAO_I {
    public List<FlowerDelivery> getAllRequests();

    public FlowerDelivery getRequest(int ID);

    public void addRequest(FlowerDelivery request);

    public void deleteRequest(int ID);
}
