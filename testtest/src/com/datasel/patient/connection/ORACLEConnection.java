package com.datasel.patient.connection;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.datasel.patient.entity.Patient;

public class ORACLEConnection {
	private static Connection connection;
	private ArrayList<Patient> patients;

	public static Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}
		String İpAdress="";
		String port="";
		String dbName="";
		String username="";
		String password="";
		connection = DriverManager.getConnection(
				"jdbc:oracle:thin:@İpAdress:port:dbName", "username", "password");
		if (connection != null) {
			System.out.println("Connected to the database!");
		} else {
			System.out.println("Failed to make connection!");
		}

		return connection;
	}

	public static void closeConnection() throws SQLException {
		connection.close();
	}


}
