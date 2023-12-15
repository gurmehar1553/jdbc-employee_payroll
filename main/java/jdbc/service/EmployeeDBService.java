package jdbc.service;

import jdbc.entity.EmployeeData;

import java.sql.*;
import java.time.format.DateTimeFormatter;
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
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
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

    public EmployeeData addEmployeeData(String name, int salary, char gender, String startDate) {
        int empId =-1;
        EmployeeData employeeData = null;
        String sql = String.format("Insert into employee_payroll (name,basic_pay,gender,start) values('%s',%d,'%s','%s');",
                name,salary,gender,startDate);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if(rowAffected == 1){
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) empId = resultSet.getInt(1);
                employeeData = new EmployeeData(empId,name,salary,Date.valueOf(startDate));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeData;
    }

    public int findSumSalaryFromDB() throws SQLException {
        String sql = "SELECT SUM(basic_pay) as sum from employee_payroll where gender='F' group by gender;";
        ResultSet resultSet = executeQueries(sql);
        while (resultSet.next()){
            return resultSet.getInt("sum");
        }
        return 0;
    }

    private ResultSet executeQueries(String sql) {
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int findMaxSalaryDB() throws SQLException {
        String sql = "Select MAX(basic_pay) as max_sal from employee_payroll where gender='F' group by gender;";
        ResultSet resultSet = executeQueries(sql);
        while (resultSet.next()){
            return resultSet.getInt("max_sal");
        }
        return 0;
    }

    public int findMinSalaryDB() throws SQLException {
        String sql = "Select MIN(basic_pay) as min_sal from employee_payroll where gender='F' group by gender;";
        ResultSet resultSet = executeQueries(sql);
        while (resultSet.next()){
            return resultSet.getInt("min_sal");
        }
        return 0;
    }
    public int findCountFemaleEmployees() throws SQLException {
        String sql = "Select COUNT(name) as cnt_female from employee_payroll where gender='F' group by gender;";
        ResultSet resultSet = executeQueries(sql);
        while (resultSet.next()){
            return resultSet.getInt("cnt_female");
        }
        return 0;
    }


    public List<EmployeeData> getEmployeeDataInDateRange(String s1, String s2) {
        List<EmployeeData> employeeDataList = new ArrayList<>();
        String sql = String.format("SELECT NAME FROM employee_payroll where start between CAST('%s' as DATE) and CAST('%s' as DATE)",s1,s2);
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String name = resultSet.getString("name");
                employeeDataList.add(new EmployeeData(name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeDataList;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "";
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
