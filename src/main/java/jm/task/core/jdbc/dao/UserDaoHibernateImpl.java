package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import static jm.task.core.jdbc.util.Util.*;
import org.hibernate.*;
import java.sql.Connection;
public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory factory;
	public UserDaoHibernateImpl() {
		factory = getSessionFactory();
    }


    @Override
    public void createUsersTable() {
		executeNative("""
			CREATE TABLE IF NOT EXISTS users (
			id BIGINT(64) PRIMARY KEY AUTO_INCREMENT,
			name VARCHAR(255),
			lastName VARCHAR(255), 
			age TINYINT(8))
		""");
    }

    @Override
    public void dropUsersTable() {
		executeNative("DROP TABLE IF EXISTS users");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
		saveUser(new User(name, lastName, age));
    }
	
	public void saveUser(User user) {
		loanSession(session -> session.save(user));
	}
	
    @Override
    public void removeUserById(long id) {
		loanSession( session -> {
			var user = session.get(User.class, id);
			if(user != null) session.delete(user);
		});
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
		loanSession( session -> 
			result.addAll(session.createQuery("from User", User.class).list()));
		return result;
    }

    @Override
    public void cleanUsersTable() {
		loanSession( session -> session.createQuery("delete from User")
										.executeUpdate());
    }
	
	private void loanSession(Consumer <Session> update) {
		try(var session = factory.openSession()) {
			session.beginTransaction();
			update.accept(session);
			session.getTransaction().commit();
		}
	}
	
	private void executeNative(String query) {
		Connection connection = getConnection();
		update(query).apply(connection);
	}
	
}
