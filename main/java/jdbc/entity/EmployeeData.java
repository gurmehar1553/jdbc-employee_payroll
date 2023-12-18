package jdbc.entity;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeData {
    public int id;
    public String name;
    public int salary;
    public Date startDate;

    public EmployeeData(String name){
        this.name = name;
    }
    public EmployeeData(int id, String name,  Date startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        EmployeeData obj = (EmployeeData) o;
        return id==obj.id && name.equals(obj.name) && Integer.compare(obj.salary,salary)==0;
    }
}
