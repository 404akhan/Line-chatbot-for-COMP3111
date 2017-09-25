package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		
		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM keyresp2");
//		stmt.setString(1, text); //or some other variables
		ResultSet rs = stmt.executeQuery();
//		return "LALSAL";
		while(rs.next()) {
			if (text.toLowerCase().contains(rs.getString(2).toLowerCase())) {
				result = rs.getString(3);
				
				String str = "UPDATE keyresp2 SET hitnum = "; 
				int v1 = rs.getInt(4)+1;
				str+= rs.getInt(4)+1;
				str+= " WHERE id = ";
				str+= rs.getInt(1);
				
				PreparedStatement stmt2 = connection.prepareStatement(str);
				stmt2.executeUpdate();

				rs.close();
				stmt.close();
				stmt2.close();
				connection.close();
				return result;
			}
		}
		rs.close();
		stmt.close();
		connection.close();
		return result;
//		
//		while (rs.next()) {
//			System.out.println("ID: " + rs.getInt(1) + "\tkeyword: " + rs.getString(2) +"\tresponse: "+ rs.getString(3));
//
//			rs.close();
//			stmt.close();
//			connection.close();
//			return rs.getString(3);
//		}
//		rs.close();
//		stmt.close();
//		connection.close();
//		return null;
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
