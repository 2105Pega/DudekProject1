package com.revature.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtils {
	
	//alternative way to find the path
//	String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("DB_Properties.properties").getPath();

	public static Connection getConnection() throws SQLException {
		try {

			Class.forName("org.postgresql.Driver"); // this will give the app the drivers needed to connect to our db

		} catch (ClassNotFoundException e) { // SQLException is a class that gets SQL
			e.printStackTrace();
		}

		String url = "jdbc:postgresql://sql-demo-database-1.cgelwtzldsxw.us-east-2.rds.amazonaws.com:5432/Sqldemo";
		String username = "postgres";
		String password = "1410Wilno";
//		String url = "";
//		String username = "";
//		String password = "";

//		try {
//
//			FileInputStream fileStream = new FileInputStream(
//					// try to change this path
//					"../JerseyJDBCBank/src/test/resources/DB_Properties.properties");
//
//			Properties prop = new Properties();
//			prop.load(fileStream);
//
//			url = prop.getProperty("URL");
//
//			username = prop.getProperty("DB_Username");
//
//			password = prop.getProperty("DB_Password");
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		catch (IOException e2) {
//			e2.printStackTrace();
//		}
//
//		catch (Exception e3) {
//			e3.printStackTrace();
//		}

		return DriverManager.getConnection(url, username, password);
	}

}
