/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Totoro
 */
public class DataProvider {

    private final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=MMT;user=sa;password=123456";
    private Connection connection = null;

    // Data Access Object
    public void connect() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(connectionString);
            if (connection != null) {
                //Logger.getLogger("connected");
                System.out.println("connected to database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(e.toString());
        }
    }

    public void disConnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Transport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Account> getAllAccount() throws SQLException{
        List<Account> accounts = new ArrayList<Account>();
        
        connect();
        
        String sql = "Select Username, Password, Fullname from Account";
        Statement statement = connection.createStatement();
        
        ResultSet rows = statement.executeQuery(sql);
        
        while(rows.next()){
            Account account = new Account(rows.getString(1), rows.getString(2), rows.getString(3));
            accounts.add(account);
        }
        
        disConnect();
        return accounts;
    }

    public Account login(String username, String password) throws SQLException {
        Account account = null;
        
        connect();
        
        String sql = "Select * from Account where Username = '" + username + "' and Password = '" + password + "'";
        Statement statement = connection.createStatement();
        
        ResultSet rs = statement.executeQuery(sql);
        rs.next();
        
        String user = rs.getString(1);
        String pass = rs.getString(2);
        String full = rs.getString(3);
        account = new Account(user, pass, full);
        
        disConnect();
        
        return account;
    }
}
