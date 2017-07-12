package com.example.core;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
 
//@Entity
@Table(name = "EMPLOYEE")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.example.core.findAll",
            query = "SELECT p FROM EMPLOYEE p"
        )
    }
)
public class Employee {
     
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank @Length(min=2, max=255)
    private String firstName;
    
    @NotBlank @Length(min=2, max=255)
    private String lastName;
    
    @Pattern(regexp=".+@.+\\.[a-z]+")
    private String email;
     
    public Employee(){
    }
 
    public Employee(Integer id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    @Override
    public String toString() {
        return "Emplyee [id=" + id + ", firstName=" + firstName + ", lastName="
                + lastName + ", email=" + email + "]";
    }
}