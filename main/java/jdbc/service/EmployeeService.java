package jdbc.service;

import jdbc.entity.EmployeeData;
import java.sql.SQLException;
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
    public boolean checkEmployeeDataSyncWithDB(String name) {
        List<EmployeeData> employeeDataList = employeeDBService.getEmployeeData(name);
        return employeeDataList.get(0).equals(name);
    }
    private EmployeeData getEmployeeData(String name) {
        return this.employeePayrollList.stream()
                .filter(data -> data.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
