package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory factory;
	public UserDaoHibernateImpl() {
		factory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
		if(factory == null) {
			factory = Util.getSessionFactory();
		}
    }

    @Override
    public void dropUsersTable() {
		if(factory != null) {
			loanSession( session -> 
				session.createSQLQuery("DROP TABLE IF EXISTS users")
						.executeUpdate());
		}
		factory = null;
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
}
