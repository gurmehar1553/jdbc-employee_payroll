package jdbc.service;

import jdbc.entity.EmployeeData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDBService {
    private PreparedStatement employeeDataStatement;
    private static EmployeeDBService employeeDBService;
    private EmployeeDBService(){}
    public static EmployeeDBService getInstance(){
        if (employeeDBService == null) return new EmployeeDBService();
        return employeeDBService;
    }
    public int updateEmployeeData(String name, int salary) {
        return this.updateEmployeeDataStatement(name,salary);
    }

    private int updateEmployeeDataStatement(String name, int salary) {
        String sql = String.format("UPDATE employee_payroll set basic_pay = %d where name ='%s'",salary,name);
        try(Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmployeeData> readData() throws SQLException {
        String sqlQuery = "SELECT * from employee_payroll;";
        List<EmployeeData> employeeDataList = new ArrayList<>();
        Connection connection = this.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        employeeDataList = this.getEmployeeData(resultSet);
        connection.close();
        return employeeDataList;
    }

    public List<EmployeeData> getEmployeeData(String name) {
        List<EmployeeData> employeeDataList = null;
        if(this.employeeDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeeDataStatement.setString(1,name);
            ResultSet resultSet = employeeDataStatement.executeQuery();
            employeeDataList = this.getEmployeeData(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employeeDataList;
    }

    private List<EmployeeData> getEmployeeData(ResultSet resultSet) {
        List<EmployeeData> employeeDataList = new ArrayList<>();
        try{
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int salary = resultSet.getInt("basic_pay");
                Date startDate = resultSet.getDate("start");
                employeeDataList.add(new EmployeeData(id,name,salary,startDate));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employeeDataList;
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            employeeDataStatement = connection.prepareStatement("SELECT * FROM employee_payroll where name = ?");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

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
