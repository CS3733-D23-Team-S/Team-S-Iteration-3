package edu.wpi.teamname.DAOs;

import java.io.IOException;
import java.util.List;

/**
 * Generalized DAO implementation class that helps with managing all of the DAOs and makes
 * implementing the facade class a lot easier
 *
 * @param <T> the type of orm to be stored in the DAO implementation
 * @param <U> the type of key used to access the data in the DAO/orm
 */
public interface IDAO<T, U> {
  /**
   * Initializes the table in the remote database according to the DAO
   *
   * @param name the name of the database table. The name of the database is stored and accessed
   *     from dbConnection and then passed to the DAO
   */
  void initTable(String name);

  /** Drops the desired table from the database in order to help reset it if needed */
  void dropTable();

  /** @param pathToCSV the path the the CSV from which to upload data to the remote */
  void loadRemote(String pathToCSV);

  /**
   * Used to import CSV files
   *
   * @param path path to the csv file to miport
   */
  void importCSV(String path);

  /**
   * Used to export a CSV that stores all of the data
   *
   * @param path path to the directory to output the CSV
   * @throws IOException in case there is some error with exporting
   */
  void exportCSV(String path) throws IOException;

  /** @return a list of all the orm nodes that store data */
  List<T> getAll();

  /**
   * gets a row from a datatable
   *
   * @param target
   */
  T getRow(U target);

  /**
   * Deletes a datapack from the DAO
   *
   * @param target the target orm to be deleted
   */
  void delete(U target);

  /**
   * Adds a datapack to the orm
   *
   * @param addition the pack
   */
  void add(T addition);
}
