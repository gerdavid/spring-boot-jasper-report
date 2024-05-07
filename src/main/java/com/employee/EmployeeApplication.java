package com.employee;

import com.employee.entity.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class EmployeeApplication {

    public static void main(String[] args) {
        try{

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "CompanyX");
            parameters.put("minSalary", 40000.0);
            parameters.put("maxSalary",150000.0);
            parameters.put("department", "HR");

            List<Employee> employees = Arrays.asList(
                    new Employee(1, "Gaurav", "IT", 150000, "2021-01-01"),
                    new Employee(2, "Raj", "HR", 80000, "2021-01-02"),
                    new Employee(3, "Rahul", "HR", 20000, "2021-01-03")
            );
            JasperPrint jasperPrint = generateEmployeeReport(employees, parameters);

        }catch(JRException e){
            System.out.println("An error occurred while generating the report: " + e.getMessage());
        }catch(Exception e){
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static JasperPrint generateEmployeeReport(List<Employee> employees, Map<String, Object> parameters) throws JRException {
    String Filepath = "C:\\Users\\Ger\\IdeaProjects\\spring-boot-jasper-report\\JasperReportsCodeTest\\employee\\src\\main\\resources";
    String department = (String) parameters.get("department");
    Double minSalary = (Double) parameters.get("minSalary");
    Double maxSalary = (Double) parameters.get("maxSalary");

    // Filter employees based on department and salary range
    List<Employee> filteredEmployees = employees.stream()
            .filter(employee -> department == null || employee.getDepartment().equals(department))
            .filter(employee -> minSalary == null || employee.getSalary() >= minSalary)
            .filter(employee -> maxSalary == null || employee.getSalary() <= maxSalary)
            .collect(Collectors.toList());

    // Load the JasperReport template and set up parameters
    // (Same as previous example)
    JasperReport jasperReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeReport.jrxml");
    // Fill the report with filtered data
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(filteredEmployees);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    JasperExportManager.exportReportToPdfFile(jasperPrint, Filepath + "\\employees.pdf");
    System.out.println("Report Generated....");
    return jasperPrint;
}

}