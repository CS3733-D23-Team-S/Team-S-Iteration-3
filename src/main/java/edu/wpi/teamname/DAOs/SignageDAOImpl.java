package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Signage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class SignageDAOImpl implements IDAO<Signage, String> {
  private final dbConnection connection;
  @Getter private ArrayList<Signage> signages;
  @Getter private HashMap<Date, ArrayList<Signage>> kiosk1;
  @Getter private HashMap<Date, ArrayList<Signage>> kiosk2;
  @Getter private String name = "hospitaldb.signage";

  public SignageDAOImpl() {
    connection = dbConnection.getInstance();
    signages = new ArrayList<>();

    kiosk1 = new HashMap<>();
    kiosk2 = new HashMap<>();
  }

  @Override
  public void initTable(String name) {
    name = this.name;
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "CREATE TABLE IF NOT EXISTS "
                      + name
                      + " (kiosklocation int, direction varchar(20), referredLocation varchar(200), date Date)");

      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error with creating the signage table");
    }
  }

  @Override
  public void add(Signage addition) {
    connection.reinitConnection();

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (kiosklocation, direction, referredLocation, date)"
                      + " VALUES (?, ?, ?, ?)");
      preparedStatement.setInt(1, addition.getKioskLocation());
      preparedStatement.setString(2, String.valueOf(addition.getDirection()));
      preparedStatement.setString(3, addition.getSurroundingLocation().getLongName());
      preparedStatement.setDate(4, addition.getThedate());

      preparedStatement.executeUpdate();

      signages.add(addition);

      HashMap<Date, ArrayList<Signage>> pointer;

      System.out.println(addition.toCSVString());

      if (addition.getKioskLocation() == 1) {
        pointer = kiosk1;
      } else {
        pointer = kiosk2;
      }

      if (pointer == null || pointer.get(addition.getThedate()) == null) {
        ArrayList<Signage> temp = new ArrayList<>();
        temp.add(addition);
        pointer.put(addition.getThedate(), temp);
      } else {
        pointer.get(addition.getThedate()).add(addition);
      }

      System.out.println("Signage added");

    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void dropTable() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String drop = "DROP TABLE IF EXISTS " + name + " CASCADE";
      stmt.executeUpdate(drop);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name + " LIMIT 2";
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the alerts from the server");
        constructFromRemote();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void constructFromRemote() {
    try {
      LocationDAOImpl locationDAO = DataBaseRepository.getInstance().getLocationDAO();
      Statement stmt = connection.getConnection().createStatement();
      String alerts = "SELECT * FROM " + name;
      ResultSet rs = stmt.executeQuery(alerts);
      while (rs.next()) {
        int kioskLocation = rs.getInt("kioskLocation");
        String direction = rs.getString("direction");
        Location referredLocation = locationDAO.get(rs.getString("referredLocation"));
        Date d = rs.getDate("date");
        Signage thisSign =
            new Signage(kioskLocation, Signage.Direction.valueOf(direction), referredLocation, d);
        signages.add(thisSign);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of nodes");
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    signages.clear();
    initTable(name);
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    path += "Node.csv";
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("nodeID,xcoord,ycoord,floor,building");
    for (Signage s : signages) {
      fileWriter.newLine();
      fileWriter.write(s.toCSVString());
    }
    fileWriter.close();
  }

  @Override
  public List<Signage> getAll() {
    return signages;
  }

  @Override
  public Signage get(String target) {
    for (int a = 0; a < signages.size(); a++) {
      if (signages.get(a).getSurroundingLocation().equals(target)) return signages.get(a);
    }

    return null;
  }

  public void deleteSignage(Date date, String loc, int k) {
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "DELETE FROM "
                      + name
                      + " WHERE kiosklocation= ? and referredlocation = ? and date = ?");
      stmt.setInt(1, k);
      // stmt.setString(2, String.valueOf(target.getDirection()));
      stmt.setString(2, loc);
      stmt.setDate(3, date);
      /*
           if (k == 1) {
             for (int a = 0; a < kiosk1.get(date).size(); a++) {
               if (kiosk1.get(date).get(a).equals(loc)) {
                 kiosk1.get(date).remove(a);
               }
             }
           } else {
             for (int a = 0; a < kiosk2.get(date).size(); a++) {
               if (kiosk2.get(date).get(a).equals(loc)) {
                 kiosk2.get(date).remove(a);
               }
             }
           }

      */

      stmt.execute();
      System.out.println("Signage deleted from database");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Signage old, Signage target) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "UPDATE "
                      + name
                      + " SET direction = ?, referredLocation = ?"
                      + " WHERE kiosklocation = ? and direction = ? and referredLocation = ? and date = ?");

      preparedStatement.setString(1, target.getDirection().name());
      preparedStatement.setString(2, target.getSurroundingLocation().getLongName());

      preparedStatement.setInt(3, old.getKioskLocation());
      preparedStatement.setString(4, old.getDirection().name());
      preparedStatement.setString(5, old.getSurroundingLocation().getLongName());
      preparedStatement.setDate(6, old.getThedate());

      preparedStatement.executeUpdate();
      System.out.println("Signage updated");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Signage> getForDateKiosk(Date d, int k) {
    ArrayList<Signage> t = new ArrayList<>();
    LocationDAOImpl locationDAO = DataBaseRepository.getInstance().getLocationDAO();

    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "SELECT * FROM "
                      + name
                      + " WHERE date = ? and kiosklocation = ? ORDER BY direction DESC");
      stmt.setDate(1, d);
      stmt.setInt(2, k);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int kl = rs.getInt("kiosklocation");
        Signage.Direction dir = Signage.Direction.valueOf(rs.getString("direction"));
        Location l = locationDAO.get(rs.getString("referredLocation"));
        Date dt = rs.getDate("date");

        Signage ds = new Signage(kl, dir, l, dt);

        t.add(ds);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return t;
  }

  @Override
  public void delete(String target) {}
}
