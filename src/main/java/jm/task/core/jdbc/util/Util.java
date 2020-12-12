package jm.task.core.jdbc.util;
import java.util.stream.Stream;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.hibernate.cfg.Environment;
public class Util {
    public static Connection getConnection() {
		var url =  "jdbc:mysql://localhost:3306/test?serverTimezone=Europe/Moscow";	
		try {
			return DriverManager.getConnection(url, "root", "root");
		} catch(Exception ex) { throw new RuntimeException(ex);}
	}
	public static SessionFactory getSessionFactory() {
		var registryBuilder = new StandardServiceRegistryBuilder();
		
		Map<String, Object> settings = new HashMap<>();
		settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		settings.put(Environment.URL, "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Europe/Moscow");
		settings.put(Environment.USER, "root");
		settings.put(Environment.PASS, "root");
		settings.put(Environment.HBM2DDL_AUTO, "update");
		
		registryBuilder.applySettings(settings);
		var registry = registryBuilder.build();
		var sources = new MetadataSources(registry);
		sources.addAnnotatedClass(jm.task.core.jdbc.model.User.class);
		var metadata = sources.getMetadataBuilder().build();
		return metadata.getSessionFactoryBuilder().build();
	}
	
	
	public static String mkString(String delimeter, String prefix, String suffix, Object...args) {
		return Stream.of(args).map(Object::toString).collect(joining(delimeter, prefix, suffix));
	}
	
	public static String asVarchar(String field) {
		return "'" + field + "'";
	}
	public static Function<Connection, Boolean> update(String query) {
		return connection -> {
			try(Statement statement = connection.createStatement()) {
				return statement.execute(query);
			} catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}
	public static Function<Connection, ResultSet> query(String query) {
		return connection -> {
			try {
				Statement statement = connection.createStatement();
				return statement.executeQuery(query);
			} catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}
}
