package pl.edu.pjwstk.lab10.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import pl.edu.pjwstk.lab10.domain.Country;

@Component
public class CountryManager implements ICountryManager{

	private Connection connection;

	//private final static String URL = "jdbc:mysql://localhost:3306/tau";
	private final static String URL = "jdbc:hsqldb:hsql://localhost/workdb";
	private final static String USER = "SA"; // SA dla hsql, root MySql
	private final static String PASSWORD = "";
	//private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String DRIVER = "com.hsqldb.jdbc.driver";
// IDENTITY lub AUTO_INCREMENT
	private String CREATE_TABLE_COUNTRY = "CREATE TABLE country( id INT NOT NULL IDENTITY , " +
			"country VARCHAR(35) NOT NULL," +
			"city VARCHAR(35) NOT NULL, " +
			"postal_code VARCHAR(10) NOT NULL, PRIMARY KEY(id))";

//	private String CREATE_TABLE_COUNTRY =
//			"CREATE TABLE Country(id bigint GENERATED BY DEFAULT AS IDENTITY, " +
//					"country varchar(30), city varchar(30), postal_code varchar(30),)";
//
	private PreparedStatement addCountryStmt; // INSERT
	private PreparedStatement getCountryStmt; // SELECT
	private PreparedStatement updateCountryStmt; // UPDATE
	private PreparedStatement deleteCountryStmt; // DELETE
	private PreparedStatement getAllCountryStmt; // SELECT ALL
	private PreparedStatement deleteAllCountryStmt; // DELETE ALL

	private Statement statement;

	public CountryManager(Connection connection) throws SQLException {
		try {
			//DriverManager.registerDriver(new org.hsqldb.jdbcDriver());
			this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			statement = this.connection.createStatement();

			ResultSet rs = this.connection.getMetaData().getTables(null, null, null,
					null);
			boolean tableExists = false;
			while (rs.next()) {
				if ("Country".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}

			if (!tableExists)
				statement.executeUpdate(CREATE_TABLE_COUNTRY);

			addCountryStmt = this.connection
					.prepareStatement("INSERT INTO country (id, country, city, postal_code) VALUES (?, ?, ?, ?)");
			getCountryStmt = this.connection
					.prepareStatement("SELECT * FROM country WHERE id = ?");
			updateCountryStmt = this.connection
					.prepareStatement("UPDATE country SET country = ?, city = ?, postal_code = ? WHERE id = ?");
			deleteCountryStmt = this.connection
					.prepareStatement("DELETE FROM country WHERE id = ?");
			getAllCountryStmt = this.connection
					.prepareStatement("SELECT * FROM country");
			deleteAllCountryStmt = this.connection
					.prepareStatement("DELETE FROM country");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() {
		return connection;
	}

	//ADD
	@Override
	public int addCountry(Country country) {
		int count = 0;
		try {
			addCountryStmt.setInt(1, (int) country.getId());
			addCountryStmt.setString(2, country.getCountry());
			addCountryStmt.setString(3, country.getCity());
			addCountryStmt.setString(4, country.getPostal_code());

			count = addCountryStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//GET
	@Override
	public Country getCountry(Country id) {
		Country c = new Country();
		int count = 0;
		try {
			ResultSet rs = getCountryStmt.executeQuery();

			while (rs.next()) {
				c.setId(rs.getInt("id"));
				c.setCountry(rs.getString("country"));
				c.setCity(rs.getString("city"));
				c.setPostal_code(rs.getString("postal_code"));
				count = 1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return c;
	}

	//UPDATE
	@Override
	public int updateCountry(Country country) {
		int count = 0;
		try {
			updateCountryStmt.setString(1, country.getCountry());
			updateCountryStmt.setString(2, country.getCity());
			updateCountryStmt.setString(3, country.getPostal_code());
			updateCountryStmt.setInt(4, (int) country.getId());
			count = updateCountryStmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//DETETE
	@Override
	public int deleteCountry(Country id) {
		int count = 0;
		try {
			count = deleteCountryStmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//GET ALL
	@Override
	public List<Country> getAllCountries() {
		List<Country> countries = new ArrayList<Country>();

		try {
			ResultSet rs = getAllCountryStmt.executeQuery();

			while (rs.next()) {
				Country c = new Country();
				c.setId(rs.getInt("id"));
				c.setCountry(rs.getString("country"));
				c.setCity(rs.getString("city"));
				c.setPostal_code(rs.getString("postal_code"));
				countries.add(c);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countries;
	}

	//DELETE ALL
	public void clearCountries() {
		try {
			deleteAllCountryStmt.executeUpdate();
			//connection.prepareStatement("DELETE FROM country").executeUpdate(); // INNY SPOSOB
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int countRow() {
		int count = 0;
		try{
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PUBLIC.COUNTRY");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				count = rs.getRow();
				//count = rs.getInt("count(*)");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		System.out.println("Number of note : " + count);
		return count;
	}

}
