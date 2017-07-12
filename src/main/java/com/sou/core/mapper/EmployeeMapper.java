package com.sou.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.sou.core.Employee;

public class EmployeeMapper implements ResultSetMapper<Employee>
{
    public Employee map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        return new Employee(resultSet.getInt("ID"), resultSet.getString("FIRSTNAME"),
        		resultSet.getString("LASTNAME"), resultSet.getString("EMAIL"));
    }
}
