package com.example.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.example.core.Employee;

import io.dropwizard.hibernate.AbstractDAO;

public class EmployeeHibernetDAO extends AbstractDAO<Employee> {

	public EmployeeHibernetDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Employee> getEmployees(Long id) {
		return Optional.ofNullable(get(id));
	}

	public Employee createEmployee(Employee employee) {
		return persist(employee);
	}

	public List<Employee> getEmployees() {
		return list(namedQuery("com.example.core.findAll"));
	}

	/*int removeEmployee(int id){
		return remove
	}

	@SqlUpdate("update EMPLOYEE set FIRSTNAME = :firstName, LASTNAME = :lastName, EMAIL = :email where ID = :id")
	int updateEmployee(@BindBean Employee Employee);
*/
}