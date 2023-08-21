package com.example.jasper.service.data;

import com.example.jasper.service.interfaces.ResultSetConnectionManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Service;

@Service
public class ResultSetConnectionManagerImpl implements ResultSetConnectionManager {

  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "org.h2.Driver";
  static final String DB_URL = "jdbc:h2:~/db-dev";

  //  Database credentials
  static final String USER = "sa";
  static final String PASS = "";

  @Override
  public ResultSet getResultSet() {
    Connection conn = null;
    Statement stmt = null;
    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      // STEP 2: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      // STEP 3: Execute a query
      System.out.println("Connected database successfully...");
      stmt = conn.createStatement();
      String sql = "SELECT country, name, date FROM HOLIDAY";

      return stmt.executeQuery(sql);
    } catch (SQLException se) {
      // Handle errors for JDBC
      se.printStackTrace();
    } catch (Exception e) {
      // Handle errors for Class.forName
      e.printStackTrace();
    }
    return null;
  }
}
