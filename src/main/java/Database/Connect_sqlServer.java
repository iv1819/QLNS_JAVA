/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Admin
 */
public class Connect_sqlServer {
        protected Connection conn = null ;

    public Connect_sqlServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
String connectionUrl = "jdbc:sqlserver://LAPTOP-35O054C8\\MSSQLSERVER01:50055;databaseName=QLNS_MVC ;encrypt=false;";
conn = DriverManager.getConnection(connectionUrl, "user1","12345");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }	
    }
}
