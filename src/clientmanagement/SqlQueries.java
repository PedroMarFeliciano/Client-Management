package clientmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlQueries {
	
	private Connection con;
	
	/**
	 * Creates a new instance of SqlQueries
	 * @param con - connection to the OracleDB
	 */
	public SqlQueries(Connection con) {
		this.con = con;
	}
	
	/**
	 * Check if tb_customer_account table exists. Creates it automatically if it doesn't.
	 */
	public void createTables() {
		
		String query = "SELECT * "
				+ "FROM ALL_TABLES "
				+ "WHERE TABLE_NAME = 'TB_CUSTOMER_ACCOUNT'";
		
		try {
			PreparedStatement statement = con.prepareStatement(query);
			
			ResultSet rs =  statement.executeQuery();
			
			if (!rs.next()) {
				System.out.println("Table 'tb_customer_account' doesn't exist.\n"
						+ "Trying to create table...");
				
				String createTable = "CREATE TABLE tb_customer_account (id_customer number(10) NOT NULL, "
						+ "cpf_cnpj varchar2(14) NOT NULL UNIQUE, nm_customer varchar2(255) NOT NULL, "
						+ "is_active number(1) NOT NULL, vl_total number(10,2) NOT NULL, PRIMARY KEY (id_customer))";
				
				statement = con.prepareStatement(createTable);
				
				statement.execute();
				
				System.out.println("Table succefully created!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	/**
	 * Inserts a new client into the DB
	 * 
	 * @param customerId - the identification of the client in the system
	 * @param cpfCnpj - its CPF or CNPJ
	 * @param nmCustomer - its name
	 * @param isActive - 1 if it is active, 0 otherwise
	 * @param vlTotal - total amount of money the client have invested
	 */
	public void insertClient(int customerId, String cpfCnpj, String nmCustomer, byte isActive, float vlTotal) {
		
		String insert = "INSERT INTO tb_customer_account (id_customer, cpf_cnpj, nm_customer, is_active, vl_total) "
				+ "VALUES (?,?,?,?,?)";
		
		try {
			PreparedStatement statement = con.prepareStatement(insert);
			
			statement.setInt(1, customerId);
			statement.setString(2, cpfCnpj);
			statement.setString(3, nmCustomer);
			statement.setByte(4, isActive);
			statement.setFloat(5, vlTotal);
			
			statement.execute();
			
		} catch (SQLException e) {
			return;
		}
	}

	/**
	 * Execute a query to search for clients that have more than 560 invested, are active and have id between 1500 and 2700
	 * @return String containing all clients that match the search and the average balance
	 */
	public String queryResult() {
		String result = "";
		
		String query = "SELECT id_customer, nm_customer, vl_total "
				+ "FROM tb_customer_account "
				+ "WHERE vl_total > 560.00 AND id_customer BETWEEN 1500 AND 2700 AND is_active = 1";
		
		try {
			PreparedStatement statement = con.prepareStatement(query);
			
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				result += rs.getInt(1) + " - ";
				result += rs.getString(2) + " - Saldo: ";
				result += rs.getFloat(3) + "\n";
				
				//clients.add(new Client(id, name, cpfCnpj, value));
			}
			
			/* The option for doing the avg calculation in SQL instead of in Java is due to the amount of data that we
			   might handle, if we would deal with a great amount of data this would be the more efficient way to do so.
			*/
			query = "SELECT ROUND(AVG(VL_TOTAL),2) " + 
					"FROM tb_customer_account " + 
					"WHERE VL_TOTAL > 560 and IS_ACTIVE = 1 and ID_CUSTOMER between 1500 and 2700";
			
			statement = con.prepareStatement(query);
			
			rs = statement.executeQuery();
			
			if (rs.next()) result += "Média: " + rs.getFloat(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(4);
		}
		
		return result;
	}
}
