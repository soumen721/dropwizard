package com.sou.core.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.sou.core.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements ResultSetMapper<Person>
{
    public Person map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        return new Person(resultSet.getInt("ID"), resultSet.getString("NAME"));
    }
}
