package jm.task.core.jdbc.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.stream.Stream;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;

public class Util {
    public static Connection getConnection() {
		var url =  "jdbc:mysql://localhost:3306/test?serverTimezone=Europe/Moscow";	
		try {
			return DriverManager.getConnection(url, "root", "root");
		} catch(Exception ex) { throw new RuntimeException(ex);}
	}
	
	public static String buildCreateQuery(String tableName, String...args) {
		var sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		var columns = mkString(",", "(", ")", args);
		sb.append(tableName).append(columns);
		return sb.toString();
	}
	public static String buildInsertQuery(String tableColumns, Object...args) {
		var sb = new StringBuilder("INSERT INTO ");
		var values = mkString(",", "(", ")", args);
		sb.append(tableColumns).append(" VALUES ").append(values);
		return sb.toString();
	}

	public static String mkString(String delimeter, String prefix, String suffix, Object...args) {
		return Stream.of(args).map(Object::toString).collect(joining(delimeter, prefix, suffix));
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
