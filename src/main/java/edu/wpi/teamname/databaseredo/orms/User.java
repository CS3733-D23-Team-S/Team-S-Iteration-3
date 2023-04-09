package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class User implements IDataPack {

	@Getter
	private String userName;
	@Setter
	private String password;
	@Getter
	Permission permission;

	public User(String name, String pass, Permission perm){
		this.userName = name;
		this.password = pass;
		this.permission = perm;
	}

	public boolean checkLogin(String password){
		return this.password.equals(password);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String toCSVString() {
		return null;
	}

	public enum Permission {
	  ADMIN,
	  WORKER,
	  GUEST,
	}
}
