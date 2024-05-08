package com.employee.entity;

public class EmployeeDetails {
    private int employeeId;
    private String address;
    private String contactInformation;

    public EmployeeDetails(int employeeId, String address, String contactInformation) {
        this.employeeId = employeeId;
        this.address = address;
        this.contactInformation = contactInformation;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getAddress() {
        return address;
    }

    public String getContactInformation() {
        return contactInformation;
    }
}

