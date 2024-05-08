package com.employee;

import com.employee.entity.Employee;
import com.employee.entity.EmployeeDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class EmployeeApplication {

    public static void main(String[] args) {
    //    SpringBootApplication.run(EmployeeApplication.class, args);
        try {
            String Filepath = "C:\\Users\\Ger\\IdeaProjects\\spring-boot-jasper-report\\JasperReportsCodeTest\\employee\\src\\main\\resources";
            // Create a list of employees with details
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee(1, "John Doe", "HR", 50000, "01/01/2000",0.0));
            employees.add(new Employee(2, "Jane Smith", "IT", 60000, "01/02/2002", 0.0));
            employees.add(new Employee(3, "Jane Doe", "HR", 70000, "01/03/2003", 0.0));
            employees.add(new Employee(4, "David Ger", "IT", 80000, "01/04/2004", 0.0));

            // Create a list of employee details
            List<EmployeeDetails> employeeDetailsList = new ArrayList<>();
            employeeDetailsList.add(new EmployeeDetails(1, "123 Main St", "John Doe's contact"));
            employeeDetailsList.add(new EmployeeDetails(2, "456 Elm St", "Jane Smith's contact"));
            employeeDetailsList.add(new EmployeeDetails(3, "789 Oak St", "Jane Doe's contact"));
            employeeDetailsList.add(new EmployeeDetails(4, "1011 Maple St", "David Ger's contact"));

            // Compile the main report
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
            JasperReport mainReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeReport.jrxml");

            // Compile subreport
            JRBeanCollectionDataSource dataSourceS = new JRBeanCollectionDataSource(employeeDetailsList);
            JasperReport subReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeDetailsSubreport.jrxml");

            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("employees", employees);
            parameters.put("createdBy", "CompanyX");

            // Fill the main report with data
             JasperPrint mainPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);

             // Create a map to hold subreport parameters
            Map<String, Object> subReportParams = new HashMap<>();

            // Iterate through each employee and fetch their details
            for (Employee employee : employees) {
                List<EmployeeDetails> filteredDetails = getEmployeeDetails(employee.getId(), employeeDetailsList);
                subReportParams.put("details", filteredDetails);

                // Add subreport to main report
                JasperPrint subPrint = JasperFillManager.fillReport(subReport, subReportParams, dataSourceS);
                mainPrint.getPages().addAll(subPrint.getPages());
            }
            
            // Export the final report
            JasperExportManager.exportReportToPdfFile(mainPrint, Filepath + "\\EmployeeReport.pdf");
            System.out.println("Report Generated....");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
    public static List<EmployeeDetails> getEmployeeDetails(int employeeId, List<EmployeeDetails> allEmployeeDetails) {
        List<EmployeeDetails> filteredEmployeeDetails = new ArrayList<>();
        for (EmployeeDetails employeeDetails : allEmployeeDetails) {
            if (employeeDetails.getEmployeeId() == employeeId) {
                filteredEmployeeDetails.add(employeeDetails);
            }
        }
        return filteredEmployeeDetails;
    }
}