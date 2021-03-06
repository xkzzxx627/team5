package schoolsystem;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccess {
	// Constants for DB access credentials
	private static final String dbUN="team5";
	private static final String dbPW="icsi518";
	private static final String dbName="csi518_school_sys";
	private static final String dbHost="localhost";
	private static final String dbPort="3306";
	private static final String dbURL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
	
	// Connection variable
	private static Connection dbConn = null;

	// Get connection
	private static boolean dbConnect(){
		if(dbConn==null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				dbConn =(Connection)DriverManager.getConnection(dbURL,dbUN,dbPW);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;
	}
	// Add user
	public static boolean addUser(User user) {
		if(!dbConnect()) {
			return false;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "INSERT INTO user (username,firstname,lastname,email,password,role) VALUES(?,?,?,?,?,3)";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getFirstname());
			statement.setString(3, user.getLastname());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getPassword());
			System.out.println(statement.toString());
			statement.executeUpdate();
			System.out.println("Successfully executed update query.");
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		}
		return true;		
	}
	// Get single user
	public static ResultSet getUser(String username) {
		ResultSet rs = null;
		
		if(!dbConnect()) {
			return null;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "SELECT * FROM user WHERE username=?";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setString(1, username);
			rs = statement.executeQuery();
			System.out.print("Successfully executed query for username=");
			System.out.println(username);
				
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	// Get user exists
	public static boolean getUserExists(String username) {
		ResultSet rs = getUser(username);
		try {
			if(rs.next()) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Get role
	public static String getRole(int roleid) {
		ResultSet rs = null;
		
		// Default to user
		String role="user";
		
		if(!dbConnect()) {
			return null;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "SELECT name FROM role WHERE idroles=?";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setInt(1, roleid);
			rs = statement.executeQuery();
			System.out.print("Successfully executed query for role=");
			System.out.println(roleid);
				
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(rs.next()) {
				role = rs.getString("name");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		return role;
	}
	// Get single user
	public static ResultSet getUserById(int id) {
		ResultSet rs = null;
		
		if(!dbConnect()) {
			return null;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "SELECT * FROM user WHERE iduser=?";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			System.out.print("Successfully executed query for iduser=");
			System.out.println(id);
				
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
		
	// Get all users
	public static ResultSet getUsers() {
		ResultSet rs = null;
		
		if(!dbConnect()) {
			return null;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "SELECT iduser,firstname,lastname FROM user WHERE NOT username='admin'";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			rs = statement.executeQuery();
			System.out.print("Successfully executed query for all users");
				
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	public static boolean CheckPassword(int uid, String current_password) {
		ResultSet rs = null;
		if(!dbConnect()) {
			return true;
		}
		try {
			System.out.println("Connected to DB");
			String sql = "Select password from user where iduser=?";
		PreparedStatement statement = dbConn.prepareStatement(sql);
		statement.setInt(1, uid);
		rs = statement.executeQuery();
		while(rs.next()) {
			if(rs.getString("password").equals(current_password)){
				System.out.print("Succesfully checked Password for user id=");
				System.out.println(uid);
				return true;
			}
			else {
				System.out.print(" current password does not match the orignal Password for user id=");
				System.out.println(uid);
				return false;
			}
		}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean UpdatePassword(int uid,String current_Password, String conf_new_Password, String new_Password) {

		int rs;
		if(!dbConnect()) {
			return false;
		}
		
		try {
			System.out.println("Connected to DB");
			if(!new_Password.equals(conf_new_Password)) {
				return false;
			}
			
			
			String sql = "update user set password = ? where iduser=?";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setString(1, new_Password);
			statement.setInt(2, uid);
			rs=statement.executeUpdate();
			if(rs==0) {
				return false;
			}

			System.out.print("Successfully updated Password for users id=");
			System.out.println(uid);
			return true;
				
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		


	}
public static String UpdateUserRole(int uid, int role) {

		
		if(!dbConnect()) {
			return null;
		}
		
		try {
			System.out.println("Connected to DB");
			String sql = "update user set role = ? where iduser=?";
			PreparedStatement statement = dbConn.prepareStatement(sql);
			statement.setInt(1, role);
			statement.setInt(2, uid);
			int i = statement.executeUpdate();
			if(i==0)
				return "No such row";
			System.out.print("Successfully updated role for idusers=");
			System.out.println(uid);
				
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "success";

	}
	
	
}
