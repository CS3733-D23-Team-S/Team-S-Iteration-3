package edu.wpi.teamname.ServiceRequests.flowers;

import java.sql.SQLException;

public interface FlowerDAO_I {
    public void init();
    public void addFlower(Flower thisFlower);
    public void deleteFlower(int ID) throws SQLException;
    public void updateFlower(int ID);
    public void loadToRemote();
    public void csvToFlower(String csvFilePath);
}
