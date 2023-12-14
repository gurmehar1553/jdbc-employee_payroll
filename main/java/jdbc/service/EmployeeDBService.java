package jdbc.service;

import jdbc.entity.EmployeeData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDBService {
    public List<EmployeeData> readData() throws SQLException {
        String sqlQuery = "SELECT * from employee_payroll;";
        List<EmployeeData> employeeDataList = new ArrayList<>();
        Connection connection = this.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int salary = resultSet.getInt("basic_pay");
            Date startDate = resultSet.getDate("start");
            employeeDataList.add(new EmployeeData(id,name,salary,startDate));
        }
        connection.close();
        return employeeDataList;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "@Gunnu123*";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Handle the exception or inform the user that the driver couldn't be loaded
        }
        Connection connection;
        System.out.println("Connecting to database: "+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL,username,password);
        System.out.println("Connection is established"+connection);
        return connection;
    }
}
