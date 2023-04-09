package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.databaseredo.orms.Move;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MoveDAOImpl implements IDAO<Move> {


	@Setter
	@Getter
	private String name;
	private dbConnection connection;

	@Getter
	HashMap<String, Move> moves = new HashMap<>();
	@Getter
	ArrayList<Move> listOfMoves = new ArrayList<>();
	@Getter
	HashMap<Integer, ArrayList<String>> nodeToLoc = new HashMap<>();

	public MoveDAOImpl() {
		connection = dbConnection.getInstance();
	}


	@Override
	public void initTable(String name) {
		this.name = name;
		String moveTable =
				"CREATE TABLE IF NOT EXISTS "
				+ name
				+ " "
				+ "(nodeID int, location varchar(100) UNIQUE, date DATE)";
		try {
			Statement stmt = connection.getConnection().createStatement();
			stmt.execute(moveTable);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error with creating the node table");
		}
	}

	@Override
	public void dropTable(String name) {

	}

	@Override
	public void loadRemote(String pathToCSV) {
		try {
			Statement stmt = connection.getConnection().createStatement();
			String checkTable = "SELECT * FROM " + name + " LIMIT 2";
			ResultSet check = stmt.executeQuery(checkTable);
			if (check.next()) {
				System.out.println("Loading the moves from the server");
				constructFromRemote();
			} else {
				System.out.println("Loading the moves to the server");
				constructRemote(pathToCSV);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importCSV(String path) {

	}

	@Override
	public void exportCSV(String path) {

	}

	@Override
	public List<Move> getAll() {
		return null;
	}

	@Override
	public void delete(Move target) {

	}

	@Override
	public void add(Move addition) {

	}

	private void constructFromRemote() {
		if (!moves.isEmpty()) {
			System.out.println("There is already stuff in the orm database");
			return;
		}
		try {
			Statement stmt = connection.getConnection().createStatement();
			String getData = "SELECT * FROM " + name;
			ResultSet data = stmt.executeQuery(getData);
			while (data.next()) {
				LocalDate date = data.getDate("date").toLocalDate();
				Move thisMove = new Move(data.getInt("nodeID"), data.getString("location"), date);
				moves.put(thisMove.getLocation(), thisMove);
				listOfMoves.add(thisMove);
				ArrayList<String> listOfLoc = new ArrayList<>();
				listOfLoc.add(thisMove.getLocation());
				nodeToLoc.put(thisMove.getNodeID(), listOfLoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getSQLState());
			System.out.println("Error accessing the remote and constructing the list of moves");
		}
	}

	private void constructRemote(String csvFilePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			try {
				reader.readLine();
				String line;
				while ((line = reader.readLine()) != null) {
					String[] fields = line.split(",");
					LocalDate date = parseDate(fields[2]);
					//          System.out.println(Arrays.toString(fields));
					Move thisMove = new Move(Integer.parseInt(fields[0]), fields[1], date);
					//          System.out.println(thisMove);
					PreparedStatement stmt =
							connection
									.getConnection()
									.prepareStatement(
											"INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
					stmt.setInt(1, Integer.parseInt(fields[0]));
					stmt.setString(2, fields[1]);
					stmt.setDate(3, java.sql.Date.valueOf(date));
					stmt.executeUpdate();
					moves.put(thisMove.getLocation(), thisMove);
				}
				constructFromRemote();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.getSQLState());
				System.out.println("Error accessing the remote and constructing the list of moves");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private LocalDate parseDate(String dateString) {
		// Parse the input date string into a LocalDate object
		LocalDate date =
				LocalDate.parse(
						dateString, DateTimeFormatter.ofPattern("M/d/yyyy").withLocale(Locale.US));
		// Format the LocalDate object as a string in the desired output format
		String outputString =
				date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US));
		// Parse the output string into a LocalDate object
		return LocalDate.parse(outputString);

	}

}
