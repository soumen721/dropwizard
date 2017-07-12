package com.example.resources;
 
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.core.Employee;
import com.example.dao.EmployeeDAO;
import com.example.dao.EmployeeHibernetDAO;
import com.example.rest.basicauth.User;

import io.dropwizard.auth.Auth;
 
@Path("/sec/employees")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EmployeeRESTControllerWithSec {
 
    private final Validator validator;
    private final EmployeeDAO employeeDAO;
    private final EmployeeHibernetDAO employeeHibernetDAO;
 
    public EmployeeRESTControllerWithSec(Validator validator, EmployeeDAO employeeDAO, EmployeeHibernetDAO employeeHibernetDAO) {
        this.validator = validator;
        this.employeeDAO = employeeDAO;
        this.employeeHibernetDAO = employeeHibernetDAO;
    }
 
    @PermitAll
    @GET
    public Response getEmployees(@Auth User user) {
        return Response.ok(employeeDAO.getEmployees()).build();
    }
 
    @RolesAllowed("USER")
    @GET
    @Path("/{id}")
    public Response getEmployeeById(@PathParam("id") Integer id,@Auth User user) {
    	//You can validate here if user is watching his record
    	/*if(id != user.getId()){
    			//Not allowed
    	}*/
        Employee employee = employeeDAO.getEmployee(id);
        if (employee != null)
            return Response.ok(employee).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }
 
    @RolesAllowed("ADMIN")
    @POST
    public Response createEmployee(@Valid Employee employee, @Auth User user) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        Employee e = employeeDAO.getEmployee(employee.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Employee> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
        	employeeDAO.updateEmployee(employee);
            return Response.created(new URI("/employees/" + employee.getId()))
                    .build();
        } else{
        	int count =  employeeDAO.createEmployee(employee);
        	return Response.ok(employee).build();
        }
            //return Response.status(Status.NOT_FOUND).build();
    }
 
    @PUT
    @Path("/{id}")
    public Response updateEmployeeById(@PathParam("id") Integer id, Employee employee) {
        // validation
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        Employee e = employeeDAO.getEmployee(employee.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Employee> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            employee.setId(id);
            employeeDAO.updateEmployee(employee);
            return Response.ok(employee).build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response removeEmployeeById(@PathParam("id") Integer id) {
        Employee employee = employeeDAO.getEmployee(id);
        if (employee != null) {
        	employeeDAO.removeEmployee(id);
            return Response.ok().build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }
}