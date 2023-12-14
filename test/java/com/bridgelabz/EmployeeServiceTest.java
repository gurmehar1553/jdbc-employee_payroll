package com.bridgelabz;

import jdbc.entity.EmployeeData;
import jdbc.service.EmployeeService;
import junit.framework.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;


public class EmployeeServiceTest {
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCountTest() throws SQLException {
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeData> employeeDataList = employeeService.readFromTheDatabase(EmployeeService.IOService.DB_IO);
        Assert.assertEquals(6,employeeDataList.size());
    }

//    @Test
//    public void
}