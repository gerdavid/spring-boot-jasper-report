package com.employee.entity;

public class Employee  {


    private int id;
    private String name;
    private String department;
    private double salary;
    private String doh;
    private Double bonus;

    public Employee(int id, String name, String department, double salary,String doh, Double bonus) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.doh = doh;
        this.bonus = bonus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }
    public String getDoh() {
        return doh;
    }

    public Double getBonus() {
        return bonus;
    }
}