package jm.task.core.jdbc.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.Stream;
import static java.util.stream.Collectors.joining;

public class Util {
    public static Connection getConnection() {
		var url = "jdbc:mysql://localhost:3306/test" +
			"?useLegacyDatetimeCode=false" +
			"&amp" +
			"&serverTimezone=UTC";
		try {
			return DriverManager.getConnection(url, "root", "root");
		} catch(Exception ex) { throw new RuntimeException(ex);}
	}
	
	public static String buildCreateQuery(String tableName, String...args) {
		var sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		var columns = Stream.of(args).collect(joining(",", "(", ")"));
		sb.append(tableName).append(columns);
		return sb.toString();
	}
	public static String buildInsertQuery(String tableColumns, Object...args) {
		var sb = new StringBuilder("INSERT INTO ");
		var values = Stream.of(args)
						.map(Object::toString)
						.collect(joining(",", "(", ")"));
		sb.append(tableColumns).append(" VALUES ").append(values);
		return sb.toString();
	}
	
}
