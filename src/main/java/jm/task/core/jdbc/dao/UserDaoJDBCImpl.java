package jm.task.core.jdbc.dao;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import jm.task.core.jdbc.model.User;
import static jm.task.core.jdbc.util.Util.*;
public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;
	public UserDaoJDBCImpl() {
		connection = getConnection();
    }

    public void createUsersTable() {
		var query = buildCreateQuery("users", 
			"id BIGINT(64) PRIMARY KEY AUTO_INCREMENT",
			"name VARCHAR(30)",
			"lastName VARCHAR(30)",
			"age TINYINT(8)");
		execute(query);
    }

    public void dropUsersTable() {
		execute("DROP TABLE IF EXISTS users");
    }
   
    public void saveUser(String name, String lastName, byte age) {
		var query = buildInsertQuery("users(name, lastName, age)", 
						  "'" + name + "'", 
						  "'" + lastName + "'", 
						  age);
		execute(query);
    }

    public void removeUserById(long id) {
		execute("DELETE FROM users WHERE id = " + id);
    }

    public List<User> getAllUsers() {
		var query = "SELECT name, lastName, age FROM users";
        try(Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);
			List<User> users = new ArrayList<>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getString("name"), 
								   resultSet.getString("lastName"),
								   resultSet.getByte("age")));
			}
			return users;
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
    }

    public void cleanUsersTable() {
		execute("DELETE FROM users");
    }
	
	private void execute(String query) {
		try(Statement statement = connection.createStatement()) {
			statement.execute(query);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
