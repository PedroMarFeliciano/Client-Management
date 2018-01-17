package clientmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Main {

	// Connection parameters
	 public static final String DBURL = "jdbc:oracle:thin:@localhost:1521:XE";
	 public static final String DBUSER = "system";
	 public static final String DBPASS = "dba";	 
	 private Connection con;
	 SqlQueries sq;
	 
	public static void main(String[] args) {
		new Main();
	}
	
	Main() {
		
		// Try to connect to OracleDB
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Login failure.\nCheck lines 11-13 in Main.java", 
					"Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}
		
		sq = new SqlQueries(con);
		
		// Verify if tb_customer_account exists. If it doesn't, creates it.
		sq.createTables();
		
		insertClients();
		queryResults();
	
		sq.queryResult();
	}
	
	/**
	 * Insert clients into tb_customer_account
	 */
	public void insertClients() {
		sq.insertClient(1499, "11111111111", "Joao da Silva", (byte) 1, 10000.00f); // not in range
		sq.insertClient(1500, "22222222222", "Maria Silveira", (byte) 1, 550.00f); // not enough money
		sq.insertClient(1501, "33333333333333", "Grupo Sucesso SA", (byte) 1, 15000.00f); // ok
		sq.insertClient(2000, "44444444444", "Ana Martins", (byte) 0, 15406.78f); // not active
		sq.insertClient(2001, "55555555555555", "Comércio Varejista LTDA", (byte) 1, 1000000.00f); // ok
		sq.insertClient(2700, "66666666666", "Helena Castro", (byte) 1, 10000.00f); // ok
		sq.insertClient(2701, "77777777777", "Roberto Figueira", (byte) 1, 845.23f); // not in range
	}
	
	/**
	 * Search for clients which are active, have > 560.00 in value and their ID are between 1500 and 2700
	 */
	public void queryResults() {
		String result = sq.queryResult();

		JOptionPane.showMessageDialog(null, result, "Resultado da Pesquisa", JOptionPane.INFORMATION_MESSAGE);
	}
}
