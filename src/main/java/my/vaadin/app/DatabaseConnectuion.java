package my.vaadin.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectuion {

	static final String DB_URL = "jdbc:mysql://localhost/VaadinDB";
	static final String USER = "root";
	static final String PASS = "root";
	private Connection conn = null;
	private Statement stmt = null;
	ResultSet resultSet = null;

	private static DatabaseConnectuion databaseInstance;

	private DatabaseConnectuion() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("SELECT * FROM Customers");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getResultSetCustomers() {
		ResultSet resultSet = null;
		try {
			resultSet = stmt.executeQuery("SELECT * FROM Customers");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet getResultSetCompanies() {
		ResultSet resultSet = null;
		try {
			resultSet = stmt.executeQuery("SELECT * FROM Companies");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static DatabaseConnectuion getInstance() {
		if (databaseInstance == null) {
			databaseInstance = new DatabaseConnectuion();
		}
		return databaseInstance;
	}

	public void addToDatabase(String firstName, String position, String email) {
		try {
			String query = "INSERT INTO Customers " + "(First_Name, Position, Email) " + "VALUES " + "('" + firstName
					+ "', '" + position + "' , '" + email + "' );";

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeFromDatabase(Long id) {
		try {

			String query = "DELETE FROM Customers WHERE id = " + id;
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateDatabase(Long id, String name, String position, String email) {
		try {

			String query = "UPDATE Customers SET First_Name = '" + name + "', Position = '" + position + "' + Email = '"
					+ email + "' WHERE id = " + id;
			// UPDATE Customers SET First_Name = 'Myrzik', Position = 'Pivovar',
			// Email = '@@@@@' WHERE id = 38;
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
