package com.cgg.callcenter.model;

/**
 * 
 * Class that represents employess' personal information
 *
 */
public class Employee {

	private EmployeeType employeeType;

	private String name;

	private String lastName;

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
