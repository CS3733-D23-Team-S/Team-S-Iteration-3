package edu.wpi.teamname.databaseredo;

import java.io.IOException;
import java.util.List;

public interface IDAO<T> {
  void initTable(String name);

  void dropTable();

  void loadRemote(String pathToCSV);

  void importCSV(String path);

  void exportCSV(String path) throws IOException;

  List<T> getAll();

  void delete(T target);

  void add(T addition);
}
