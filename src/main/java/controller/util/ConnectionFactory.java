package controller.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	 public Connection getConnection() {
	     try {
	         return DriverManager.getConnection("DATABASE", "USER", "PASSWORD");
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	 }

}
