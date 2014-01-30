package com.codepath.example.firstdemo;

import java.util.ArrayList;

public class User {
	public String name;
	public String hometown;
	
	public User(String name, String hometown) {
		this.name = name;
		this.hometown = hometown;
	}
	
	public static ArrayList<User> getFakeUsers() {
		
		ArrayList<User> users = new ArrayList<User>();
		
		users.add(new User("vishal", "vishal's home"));
		users.add(new User("kapoor", "kapoor's home"));
		users.add(new User("rama", "rama's home"));
		
		return users;
	}
	
	public String toString() {
		return name + " - " + hometown;
	}
}
