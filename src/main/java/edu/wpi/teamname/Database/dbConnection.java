package edu.wpi.teamname.Database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
	private static dbConnection single_instance;

	Connection c;
	private static final String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamsdb";
	private static final String user = "teams";
	private static final String password = "teams160";
	static final String schemaName = "hospitaldb";
	@Getter
	static final String nodeTable = schemaName + "." + "nodes";
	@Getter
	static final String edgesTable = schemaName + "." + "edges";
	@Getter
	static final String moveTable = schemaName + "." + "moves";
	@Getter
	static final String locationTable = schemaName + "." + "locations";
	@Getter
	static final String foodTable = schemaName + "." + "foods";

	private dbConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	// Static method
	// Static method to create instance of Singleton class
	public static synchronized dbConnection getInstance() {
		if (single_instance == null) single_instance = new dbConnection();
		return single_instance;
	}

	public Connection getConnection() {
		return c;
	}
}
