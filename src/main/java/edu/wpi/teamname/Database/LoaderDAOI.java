package edu.wpi.teamname.Database;

import java.io.IOException;
import java.sql.SQLException;

public interface LoaderDAOI {
  void load() throws SQLException;

  void resetData() throws SQLException;

  // Add more imports as needed
  void loadCSV(String importPath, char dataType);

  void exportCSVs(String exportPath) throws IOException;
}
