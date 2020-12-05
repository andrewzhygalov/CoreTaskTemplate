package jm.task.core.jdbc;
import jm.task.core.jdbc.service.*;
import jm.task.core.jdbc.model.*;
import java.util.List;
public class Main {
	static UserService service  = new UserServiceImpl();
    public static void main(String[] args) throws Exception {
		service.createUsersTable();
		List.of(user("Ivan", "Ivanov", 25), 
			user("Lena", "Petrova", 21), 
			user("Petr", "Semenov", 34), 
			user("Anna", "Sidorova", 42))
			.forEach(Main::logDBInsertion);
		service.getAllUsers().forEach(System.out::println);
		service.cleanUsersTable();
		service.dropUsersTable();
    }
	static User user(String name, String lastName, int age) {
		return new User(name, lastName, (byte) age);
	}
	static void logDBInsertion(User user) {
		service.saveUser(user.getName(), user.getLastName(), user.getAge());
		System.out.println(user + " added to database");
	}
}
