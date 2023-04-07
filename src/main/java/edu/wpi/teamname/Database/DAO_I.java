package edu.wpi.teamname.Database;

import java.sql.SQLException;

public interface DAO_I {

  void initTables() throws SQLException;

  //  boolean checkTablePresence() throws SQLException;

  void resetData() throws SQLException;
}
