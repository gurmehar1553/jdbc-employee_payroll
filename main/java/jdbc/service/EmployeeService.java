package jdbc.service;

import jdbc.entity.EmployeeData;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private EmployeeDBService employeeDBService;
    public enum IOService{CONSOLE_IO, FILE_IO,DB_IO}
    private List<EmployeeData> employeePayrollList;
    public EmployeeService(){
        employeeDBService = EmployeeDBService.getInstance();
    }
    public EmployeeService(List<EmployeeData> employeeDataList){
        this();
        this.employeePayrollList = employeeDataList;
    }

    /**
     * Read data from database
     * @param ioService
     * @return
     * @throws SQLException
     */
    public List<EmployeeData> readFromTheDatabase(EmployeeService.IOService ioService) throws SQLException {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeeDBService.readData();
        return this.employeePayrollList;
    }
    public void updateEmployeeData(String name, int salary) {
        int result = employeeDBService.updateEmployeeData(name,salary);
        if (result == 0) return;
        EmployeeData employeeData = this.getEmployeeData(name);
        if (employeeData != null) employeeData.salary = salary;
    }

    /**
     * Chech data in memory is sync
     * with database
     * @param name
     * @return
     */
    public boolean checkEmployeeDataSyncWithDB(String name) {
        List<EmployeeData> employeeDataList = employeeDBService.getEmployeeData(name);
        System.out.println(employeeDataList.get(0).name);
        return employeeDataList.get(0).name.equals(name);
    }
    private EmployeeData getEmployeeData(String name) {
        return this.employeePayrollList.stream()
                .filter(data -> data.name.equals(name))
                .findFirst()
                .orElse(null);
    }
    public void addEmployee(String name, int salary, char gender, String startDate) {
        employeePayrollList.add(employeeDBService.addEmployeeDataUC7(name,salary,gender,startDate));
    }
    public void addEmployeePayroll(String name, int salary, char gender, String startDate) {
        employeePayrollList.add(employeeDBService.addEmployeePayrollData(name,salary,gender,startDate));
    }

    public int findSumSalary(){
        int sum_salary;
        try {
            sum_salary= employeeDBService.findSumSalaryFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sum_salary;
    }
    public int findMaxSalary(){
        int max_salary;
        try {
            max_salary= employeeDBService.findMaxSalaryDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return max_salary;
    }
    public int findMinSalary(){
        int min_salary;
        try {
            min_salary= employeeDBService.findMinSalaryDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return min_salary;
    }
    public int findCountOfEmployees(){
        int count;
        try {
            count = employeeDBService.findCountFemaleEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public List<EmployeeData> retrieveEmpDataInDateRange(String s1, String s2) {
        return employeeDBService.getEmployeeDataInDateRange(s1,s2);
    }
}
