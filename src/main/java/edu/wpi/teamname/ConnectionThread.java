package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionThread extends Thread {
	public void run() {

		long deltaTime = 0;

		while (true) {
			if (System.currentTimeMillis() - deltaTime > 60000) {
				deltaTime = System.currentTimeMillis();
				Connection connection = dbConnection.getInstance().getConnection();
				try {
					if (connection.isClosed()) {
						dbConnection.getInstance().reinitConnection();
						System.out.println("Forcing an update and checking the connection");
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
								DataBaseRepository.getInstance().forceGlobalUpdate();

			}
		}
	}
}
