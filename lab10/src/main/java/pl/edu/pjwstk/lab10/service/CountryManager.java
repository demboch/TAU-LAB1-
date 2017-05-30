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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CountryManager implements ICountryManager{

	private Connection connection;

	//private final static String URL = "jdbc:mysql://localhost:3306/tau";
	private String URL = "jdbc:hsqldb:hsql://localhost/workdb";
	private String USER = "SA"; // SA dla hsql, root MySql
	private String PASSWORD = "";
	//private final static String DRIVER = "com.mysql.jdbc.Driver";
	//private final static String DRIVER = "com.hsqldb.jdbc.driver";

//	private String CREATE_TABLE_COUNTRY = "CREATE TABLE country( id INT NOT NULL IDENTITY , " +
//			"country VARCHAR(35) NOT NULL," +
//			"city VARCHAR(35) NOT NULL, " +
//			"postal_code VARCHAR(10) NOT NULL, PRIMARY KEY(id))";

	private String CREATE_TABLE_COUNTRY = "CREATE TABLE country(id bigint GENERATED BY DEFAULT AS IDENTITY, country varchar(35), city varchar(35), postal_code varchar(20) NOT NULL UNIQUE)";

	private PreparedStatement addCountryStmt; // INSERT
	private PreparedStatement getCountryStmt; // SELECT
	private PreparedStatement updateCountryStmt; // UPDATE
	private PreparedStatement deleteCountryStmt; // DELETE
	private PreparedStatement getAllCountryStmt; // SELECT ALL
	private PreparedStatement deleteAllCountryStmt; // DELETE ALL

	private Statement statement;

	public CountryManager(){}

	public CountryManager(Connection connection) throws SQLException {
		try {
			//DriverManager.registerDriver(new org.hsqldb.jdbcDriver());
			//this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
			this.connection = connection;
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
					.prepareStatement("INSERT INTO country (country, city, postal_code) VALUES (?, ?, ?)");
			getCountryStmt = this.connection
					.prepareStatement("SELECT * FROM country WHERE city = ?");
			updateCountryStmt = this.connection
					.prepareStatement("UPDATE country SET country = ?, postal_code = ? WHERE city = ?");
			deleteCountryStmt = this.connection
					.prepareStatement("DELETE FROM country WHERE city = ?");
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
	public int addCountry(Country country) throws SQLException {
		int count = 0;
		try {
			//addCountryStmt.setInt(1, country.getId());
			addCountryStmt.setString(1, country.getCountry());
			addCountryStmt.setString(2, country.getCity());
			addCountryStmt.setString(3, country.getPostal_code());

			count = addCountryStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	//GET
	@Override
	public Country getCountryFromDB(Country country) throws SQLException {
		Country c = null;
		int count = 0;
		try {
			getCountryStmt.setString(1,country.getCity());
			ResultSet rs = getCountryStmt.executeQuery();

			while (rs.next()) {
				c = new Country();
				c.setId(rs.getInt("id"));
				c.setCountry(rs.getString("country"));
				c.setCity(rs.getString("city"));
				c.setPostal_code(rs.getString("postal_code"));
				//count = 1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

	//UPDATE
	@Override
	public int updateCountry(Country country) throws SQLException {
//		int count = 0;
//		try {
			updateCountryStmt.setString(1, country.getCountry());
			updateCountryStmt.setString(2, country.getCity());
			updateCountryStmt.setString(3, country.getPostal_code());
			//updateCountryStmt.setInt(4, country.getId());
			return updateCountryStmt.executeUpdate();
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return count;
	}

	//DETETE
	@Override
	public int deleteCountry(Country country) throws SQLException {
		deleteCountryStmt.setString(1, country.getCity());
		return deleteCountryStmt.executeUpdate();
//		int count = 0;
//		try {
//			count = deleteCountryStmt.executeUpdate();
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return count;
	}

	//GET ALL
	@Override
	public List<Country> getAllCountries() throws SQLException {
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
	public void clearCountries() throws SQLException {
		try {
			//deleteAllCountryStmt.executeUpdate();
			connection.prepareStatement("DELETE FROM country").executeUpdate(); // INNY SPOSOB
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public int countRow() throws SQLException {
//		int count = 0;
//		try{
//			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PUBLIC.COUNTRY");
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				count = rs.getRow();
//				//count = rs.getInt("count(*)");
//			}
//		}catch (SQLException e){
//			e.printStackTrace();
//		}
//		System.out.println("Number of country : " + count);
//		return count;
//	}

}
