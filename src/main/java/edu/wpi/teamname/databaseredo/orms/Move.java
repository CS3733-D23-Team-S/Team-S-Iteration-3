package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Move implements IDataPack {
	@Getter
	private int nodeID;
	@Getter
	private String location;
	@Getter
	private LocalDate date;

	public Move(int nodeID, String location, LocalDate date) {
		this.nodeID = nodeID;
		this.date = date;
		this.location = location;
	}

	@Override
	public String toString() {
		return "Move{"
			   + "nodeID = "
			   + nodeID
			   + ", location = '"
			   + location
			   + ", date = "
			   + date.toString()
			   + '}';
	}

	@Override
	public String toCSVString() {
		return nodeID + "," + location + "," + date;
	}
}
