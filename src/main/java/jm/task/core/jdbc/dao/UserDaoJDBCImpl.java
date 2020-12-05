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
		update(query).apply(connection);
    }

    public void dropUsersTable() {
		update("DROP TABLE IF EXISTS users").apply(connection);
    }
   
    public void saveUser(String name, String lastName, byte age) {
		var query = buildInsertQuery("users(name, lastName, age)", 
						  "'" + name + "'", 
						  "'" + lastName + "'", 
						  age);
		update(query).apply(connection);
    }

    public void removeUserById(long id) {
		update("DELETE FROM users WHERE id = " + id).apply(connection);
    }

    public List<User> getAllUsers() {
		var query = "SELECT name, lastName, age FROM users";
        try {
			ResultSet resultSet = query(query).apply(connection);
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
		update("DELETE FROM users").apply(connection);
    }
}
