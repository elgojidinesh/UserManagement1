package com.xadmin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;

import com.xadmin.usermanagement.bean.User;

public class UserDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/timepass?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

	private static final String INSERT_USERS_SQL = "INSERT INTOUSERS" + " (name,email,country)VALUES" + "(?,?,?);";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?;";
	private static final String UPDATE_USERS_SQL = "update users set name=?,email=?,country=? where id=?;";

	public UserDao() {
	}

	protected Connection getConnection() throws ClassNotFoundException {
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
//insert user
	public void insertUser(User user) throws ClassNotFoundException {
		System.out.println(INSERT_USERS_SQL);
		try (Connection connection = getConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedstatement.setString(1, user.getName());
			preparedstatement.setString(2, user.getEmail());
			preparedstatement.setString(3, user.getCountry());
			System.out.println(preparedstatement);
			preparedstatement.executeUpdate();

		} catch (SQLException e) {
			printSQlException(e);
		}

	}
//select user byid
	public User selectUser(int id) throws ClassNotFoundException {
		User user = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);

			}
		}

		catch (SQLException e) {
			printSQlException(e);
		}
		return user;
	}

//select all the users
public List<User> selectAllUsers() throws ClassNotFoundException, SQLException{
	List<User>users=new ArrayList<>();
	try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
		System.out.println(preparedStatement);
		ResultSet rs=preparedStatement.executeQuery();
		while(rs.next()) {
			int id=rs.getInt("id");
			String name=rs.getString("name");
			String email=rs.getString("email");
			String Country=rs.getString("Country");
			users.add(new User(id, name, email, Country));
			
		}
	}catch (SQLException e) {
  printSQlException(e);
	}
	return users;
}
//update user detailes
public boolean updateUser(User user) throws ClassNotFoundException, SQLException {
	boolean rowUpdated;
	try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
		System.out.println("updated USer:"+statement);
		statement.setString(1, user.getName());
		statement.setString(1, user.getEmail());
		statement.setString(1, user.getCountry());
		statement.setInt(1, user.getId());
		rowUpdated=statement.executeUpdate()>0;
}
	return rowUpdated;
	}
//delete user
public boolean deleteUser(int id) throws ClassNotFoundException, SQLException {
	boolean rowDeleted;
	try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
		statement.setInt(1, id);
		rowDeleted=statement.executeUpdate()>0;
	}
	return rowDeleted;
	
	
}

	private void printSQlException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState:" + ((SQLException) e).getSQLState());
				System.err.println("Error code:" + ((SQLException) e).getErrorCode());
				System.err.println("SQLState:" + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("cause:" + t);
					t = t.getCause();
				}
			}

		}

	}

}
