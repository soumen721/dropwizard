package com.example.dao;
 
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.example.core.Employee;
import com.example.core.mapper.EmployeeMapper;
 

@RegisterMapper(EmployeeMapper.class)
public interface EmployeeDAO {

    @SqlQuery("select * from EMPLOYEE")
    List<Employee> getEmployees();

    @SqlQuery("select * from EMPLOYEE where ID = :id")
    Employee getEmployee(@Bind("id") int id);

    @SqlUpdate("delete from EMPLOYEE where ID = :id")
    int removeEmployee(@Bind("id") int id);

    @SqlUpdate("update EMPLOYEE set FIRSTNAME = :firstName, LASTNAME = :lastName, EMAIL = :email where ID = :id")
    int updateEmployee(@BindBean Employee Employee);

    @SqlUpdate("insert into EMPLOYEE (ID, FIRSTNAME, LASTNAME, EMAIL) values (:id, :firstName, :lastName, :email)")
    int createEmployee(@BindBean Employee employee);
}

/*public class EmployeeDAO {
     
    public static HashMap<Integer, Employee> employees = new HashMap<>();
    static{
        employees.put(1, new Employee(1, "Lokesh", "Gupta", "India"));
        employees.put(2, new Employee(2, "John", "Gruber", "USA"));
        employees.put(3, new Employee(3, "Melcum", "Marshal", "AUS"));
    }
     
    public static List<Employee> getEmployees(){
        return new ArrayList<Employee>(employees.values());
    }
     
    public static Employee getEmployee(Integer id){
        return employees.get(id);
    }
     
    public static void updateEmployee(Integer id, Employee employee){
        employees.put(id, employee);
    }
     
    public static void removeEmployee(Integer id){
        employees.remove(id);
    }
    
    public static Employee createEmployee(Employee emp){
    	employees.put(emp.getId(), emp);
        return emp;
    }
}*/