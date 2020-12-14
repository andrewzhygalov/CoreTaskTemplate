package jm.task.core.jdbc.dao;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import jm.task.core.jdbc.model.User;
import static jm.task.core.jdbc.util.Util.*;
import jm.task.core.jdbc.util.Operations;
public class UserDaoJDBCImpl implements UserDao {
	private static User resultSetToUserMapping(ResultSet resultSet) {
		try {
			return new User(resultSet.getString("name"), resultSet.getString("lastName"),
								   resultSet.getByte("age"));
		} catch(Exception ex) { throw new RuntimeException(ex); }
	}
	
	public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
		var query = """
			CREATE TABLE IF NOT EXISTS users (
			id BIGINT(64) PRIMARY KEY AUTO_INCREMENT,
			name VARCHAR(30),
			lastName VARCHAR(30), 
			age TINYINT(8))
		""";
		Operations.executeUpdate(query);
    }

    public void dropUsersTable() {
		Operations.executeUpdate("DROP TABLE IF EXISTS users");
    }
    
	public void saveUser(User user) {
		saveUser(user.getName(), user.getLastName(), user.getAge());
	}
    public void saveUser(String name, String lastName, byte age) {
		var insert = "INSERT INTO users(name, lastName, age) VALUES ";
		var values = mkString(", ", "(", ")", asVarchar(name), asVarchar(lastName), age);
		Operations.executeUpdate(insert + values);
    }

    public void removeUserById(long id) {
		Operations.executeUpdate("DELETE FROM users WHERE id = " + id);
    }

    public List<User> getAllUsers() {
		var query = "SELECT name, lastName, age FROM users";
        return Operations.collect(query, UserDaoJDBCImpl::resultSetToUserMapping);
    }

    public void cleanUsersTable() {
		Operations.executeUpdate("DELETE FROM users");
    }
}
