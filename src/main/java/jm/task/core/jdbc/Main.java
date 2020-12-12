package jm.task.core.jdbc;
import jm.task.core.jdbc.service.*;
import jm.task.core.jdbc.model.*;
import jm.task.core.jdbc.dao.*;
import static jm.task.core.jdbc.util.Util.*;
import java.util.List;

public class Main {
	static UserService service  = new UserServiceImpl();
    public static void main(String[] args) throws Exception {
		service.createUsersTable();
		List.of(new User("Ivan", "Ivanov", (byte)25), 
			new User("Lena", "Petrova", (byte) 21), 
			new User("Petr", "Semenov", (byte)34), 
			new User("Anna", "Sidorova", (byte)42))
			.forEach(Main::logDBInsertion);
		service.getAllUsers().forEach(System.out::println);
		service.cleanUsersTable();
		service.dropUsersTable();  
    }
	
	static void logDBInsertion(User user) {
		service.saveUser(user);
		System.out.println(user + " added to database");
	}
}
