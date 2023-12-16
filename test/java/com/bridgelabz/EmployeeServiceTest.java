package com.bridgelabz;

import jdbc.entity.EmployeeData;
import jdbc.service.EmployeeService;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EmployeeServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCountTest() throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeData> employeeDataList = employeeService.readFromTheDatabase(EmployeeService.IOService.DB_IO);
        Assert.assertEquals(6,employeeDataList.size());
    }

    @Test
    public void givenEmployeeSalaryUpdated_ShouldSyncWithDB() throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeData> employeeDataList = employeeService.readFromTheDatabase(EmployeeService.IOService.DB_IO);
        employeeService.updateEmployeeData("John",3000000);
        boolean result = employeeService.checkEmployeeDataSyncWithDB("John");
        Assert.assertTrue(result);
    }
    @Test
    public void retrieveAllEmployeesInParticularDateRange_FromDB() {
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeData> employeeDataList = employeeService.retrieveEmpDataInDateRange("2021-07-02","2021-08-10");
        Assert.assertEquals(3,employeeDataList.size());
    }
    @Test
    public void sqlSUM_MAX_MIN_COUNT(){
        Assert.assertEquals(9200000,new EmployeeService().findSumSalary());
        Assert.assertEquals(4,new EmployeeService().findCountOfEmployees());
        Assert.assertEquals(1000000,new EmployeeService().findMinSalary());
        Assert.assertEquals(3000000,new EmployeeService().findMaxSalary());
    }
    @Test
    public void givenNewEmployeeWhenAdded_SyncWithDB() throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        employeeService.readFromTheDatabase(EmployeeService.IOService.DB_IO);
        employeeService.addEmployeePayroll("Mary",50000000,'F',"2017-10-01");
        boolean result = employeeService.checkEmployeeDataSyncWithDB("Mary");
        Assert.assertTrue(result);
    }

}