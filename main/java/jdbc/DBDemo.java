package jdbc;

import jdbc.service.EmployeeService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DBDemo {
    public static void main(String[] args){
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "";
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        listDrivers();
        System.out.println("Connecting to database "+jdbcURL);
        try {
            connection = DriverManager.getConnection(jdbcURL,username,password);
            System.out.println("Connection is successful !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listDrivers() {
        Enumeration<Driver> driver = DriverManager.getDrivers();
        while (driver.hasMoreElements()){
            Driver driverClass = (Driver) driver.nextElement();
            System.out.println(driverClass);
        }
    }
}
