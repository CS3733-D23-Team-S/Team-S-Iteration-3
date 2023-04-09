package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.databaseredo.orms.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ActiveUser {

	@Getter
	private HashSet<String> activeUsers;
	private dbConnection connection;

	private static ActiveUser single_instance = null;

	private ActiveUser(){
		this.activeUsers = new HashSet<>();
	}

	public static synchronized ActiveUser getInstance(){
		if(single_instance == null) single_instance = new ActiveUser();
		return single_instance;
	}

	public boolean checkUserActive(String username){
		return activeUsers.contains(username);
	}

}
