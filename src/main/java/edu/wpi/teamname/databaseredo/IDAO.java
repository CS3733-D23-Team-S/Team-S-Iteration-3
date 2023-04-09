package edu.wpi.teamname.databaseredo;

import java.util.List;

public interface IDAO<T> {
	void initTable(String name);
	void dropTable(String name);
	void loadRemote(String pathToCSV);
	void importCSV(String path);
	void exportCSV(String path);
	List<T> getAll();
	void delete(T target);
	void add(T addition);


}
