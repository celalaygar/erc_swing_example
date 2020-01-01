package com.datasel.patient.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.datasel.patient.entity.Patient;

public class DbHelper {

	private ArrayList<Patient> patients;
	private Connection connection;
	private PreparedStatement stmt;

	public DbHelper() throws SQLException {
		connection = ORACLEConnection.getConnection();
	}

	public ArrayList<Patient> getAllDetail() throws SQLException {

		patients = new ArrayList<Patient>();
		String sql = "SELECT * FROM AAPATIENT ORDER BY patientid";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Patient p = new Patient(Integer.valueOf(rs.getString("patientid")),
					rs.getString("name"), rs.getString("lastname"),
					rs.getString("gender"), rs.getString("city"),
					rs.getString("age"));
			patients.add(p);
		}
		return patients;
	}

	public Patient addPatient(Patient patient) throws SQLException {
		if (patient != null) {
			String sql = "INSERT INTO AAPATIENT VALUES (?,?,?,?,?,?)";
			stmt = connection.prepareStatement(sql);
			int id = databaseId();
			stmt.setInt(1, id);
			stmt.setString(2, patient.getName());
			stmt.setString(3, patient.getSurname());
			stmt.setString(4, patient.getAge());
			stmt.setString(5, patient.getGender());
			stmt.setString(6, patient.getCity());
			patient.setId(id);
			int i = stmt.executeUpdate();
			System.out.println(i + " records inserted");
			return patient;
		} else {
			System.out.println("Patient is null");
			return null;
		}
	}
	
	public Patient addPatientFromObject(Patient patient) throws SQLException {
		if (patient != null) {
			String sql = "INSERT INTO AAPATIENT VALUES (?,?,?,?,?,?)";
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, patient.getId());
			stmt.setString(2, patient.getName());
			stmt.setString(3, patient.getSurname());
			stmt.setString(4, patient.getAge());
			stmt.setString(5, patient.getGender());
			stmt.setString(6, patient.getCity());
			int i = stmt.executeUpdate();
			System.out.println(i + " records inserted");
			return patient;
		} else {
			System.out.println("Patient is null");
			return null;
		}
	}
	
	public void deletePatient(String id) throws SQLException {
		System.out.println("id : " + id);
		String sql = "DELETE from AAPATIENT where patientid=?";
		stmt = connection.prepareStatement(sql);
		stmt.setInt(1, Integer.valueOf(id));
		int row = stmt.executeUpdate();
		System.out.println("Patient deleted with patientid : " + row);
	}

	public int databaseId() throws SQLException {
		int myId = 0;
		String sqlIdentifier = "select AA_PATIENT_SEQ.NEXTVAL from DUAL";
		PreparedStatement pst = connection.prepareStatement(sqlIdentifier);
		synchronized (this) {
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				myId = rs.getInt(1);
			}
		}
		return myId;
	}

	public Patient updatePatient(Patient patient) throws SQLException {
		PreparedStatement ps = connection
				.prepareStatement("UPDATE AAPATIENT SET " + "name = ?, "
						+ "lastname = ?, " + "gender = ?, " + "city = ?, "
						+ "age = ? " + "WHERE patientid = ? ");

		ps.setString(1, patient.getName());
		ps.setString(2, patient.getSurname());
		ps.setString(3, patient.getGender());
		ps.setString(4, patient.getCity());
		ps.setString(5, patient.getAge());
		ps.setInt(6, patient.getId());
		ps.executeUpdate();
		return patient;
	}
	public Patient updateDirtyPatient(Patient patient) throws SQLException {
		PreparedStatement ps = connection
				.prepareStatement("UPDATE AAPATIENT SET " + "name = ?, "
						+ "lastname = ?, " + "gender = ?, " + "city = ?, "
						+ "age = ? " + "WHERE patientid = ? ");
		if(patient.isDirty()){
			ps.setString(1, patient.getName());
			ps.setString(2, patient.getSurname());
			ps.setString(3, patient.getGender());
			ps.setString(4, patient.getCity());
			ps.setString(5, patient.getAge());
			ps.setInt(6, patient.getId());
			ps.executeUpdate();
		}
		return patient;
	}
	
	public void deleteSelectedPatient(ArrayList<Patient> patients2)  throws SQLException {
		String result ="Delete proccesing was failed !";
		String sql = "DELETE from AAPATIENT where patientid=?";
		//patients2.forEach(System.out::println);
		for (Patient patient : patients2) {
			if(patient.isSelected()){
				stmt = connection.prepareStatement(sql);
				stmt.setInt(1, patient.getId());
				stmt.executeUpdate();
			}
		}
	}
}
