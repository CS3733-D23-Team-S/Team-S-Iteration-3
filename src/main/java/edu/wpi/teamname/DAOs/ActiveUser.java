package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.User;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class ActiveUser {

	@Getter
	@Setter
	User currentUser;

	@Getter
	Permission permission;

	@Getter
	@Setter
	private boolean loggedIn = false;


	private ArrayList<IDataPack> cart;

	private static ActiveUser single_instance = null;

	private ActiveUser() {
		cart = new ArrayList<>();
	}

	public static synchronized ActiveUser getInstance() {
		if (single_instance == null) single_instance = new ActiveUser();
		return single_instance;
	}

	public void addToCart(IDataPack item) {
		cart.add(item);
	}

	public void removeFromCart(IDataPack item) {
		cart.remove(item);
	}
}
