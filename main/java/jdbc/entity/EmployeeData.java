package jdbc.entity;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeData {
    public int id;
    public String name;
    public int salary;
    public Date startDate;

    public EmployeeData(int id, String name, int salary, Date startDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }
}
