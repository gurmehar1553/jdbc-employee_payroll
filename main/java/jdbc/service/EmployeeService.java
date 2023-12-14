package jdbc.service;

import jdbc.entity.EmployeeData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    public enum IOService{CONSOLE_IO, FILE_IO,DB_IO}
    private List<EmployeeData> employeePayrollList;
    public List<EmployeeData> readFromTheDatabase(EmployeeService.IOService ioService) throws SQLException {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = new EmployeeDBService().readData();
        return this.employeePayrollList;
    }
}
