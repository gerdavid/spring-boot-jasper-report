package com.employee;

import com.employee.entity.Employee;
import com.employee.entity.EmployeeDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootApplication
public class EmployeeApplication {

    public static void main(String[] args) {
        try {
            String Filepath = "C:\\Users\\Ger\\IdeaProjects\\spring-boot-jasper-report\\JasperReportsCodeTest\\employee\\src\\main\\resources";
            // Create a list of employees with details
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee(1, "John Doe", "HR", 50000, "01/01/2000",0.0));
            employees.add(new Employee(2, "Jane Smith", "IT", 60000, "01/02/2002", 0.0));
            employees.add(new Employee(3, "Jane Doe", "HR", 70000, "01/03/2003", 0.0));
            employees.add(new Employee(4, "David Ger", "IT", 80000, "01/04/2004", 0.0));
            employees.add(new Employee(5, "Mike Joseph", "IT", 90000, "01/05/2005", 0.0));
            employees.add(new Employee(6, "Jack Bauer", "IT", 100000, "01/06/2006", 0.0));

            // Create a list of employee details
            List<EmployeeDetails> employeeDetailsList = new ArrayList<>();
            employeeDetailsList.add(new EmployeeDetails(1, "123 Main St", "John Doe's contact 022222"));
            employeeDetailsList.add(new EmployeeDetails(2, "456 Elm St", "Jane Smith's contact 033333"));
            employeeDetailsList.add(new EmployeeDetails(3, "789 Oak St", "Jane Doe's contact 044444"));
            employeeDetailsList.add(new EmployeeDetails(4, "1011 Maple St", "David Ger's contact 055555"));
            employeeDetailsList.add(new EmployeeDetails(5, "1213 Oak St", "Mike Joseph's contact 066666"));
            employeeDetailsList.add(new EmployeeDetails(6, "1415 Maple St", "Jack Bauer's contact 077777"));

            // Apply filtering based on department and salary range
            List<Employee> filteredEmployees = new ArrayList<>();
            String department1 = "IT";
            double minSalary = 40000.0;
            double maxSalary = 150000.0;
            for (Employee employee : employees) {

                if (employee.getDepartment().equals(department1) && employee.getSalary() >= minSalary && employee.getSalary() <= maxSalary) {
                    filteredEmployees.add(employee);
            }
}
            // Compile the main report
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(filteredEmployees);
            JasperReport mainReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeReport.jrxml");


            // Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("employees", employees);
            parameters.put("createdBy", "CompanyX");
            parameters.put("companyAddress","Nairobi-001");


            Map<String, Double> totalSalaryByDepartment = calculateTotalSalaryByDepartment(employees);
            // Prepare parameters
            parameters.put("totalSalaryByDepartment", totalSalaryByDepartment);

            // Calculate average salary by department
            Map<String, Double> averageSalaryByDepartment = calculateAverageSalaryByDepartment(employees);
            // Prepare parameters
            parameters.put("averageSalaryByDepartment", averageSalaryByDepartment);

            // Fill the main report with data
            JasperPrint mainPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);

            // Iterate through each employee and fetch their details
            for (Employee employee : filteredEmployees) {
                List<EmployeeDetails> filteredDetails = getEmployeeDetails(employee.getId(), employeeDetailsList);

                // Compile subreport
                JRBeanCollectionDataSource dataSourceS = new JRBeanCollectionDataSource(filteredDetails);
                JasperReport subReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeDetailsSubreport.jrxml");

                // Create a map to hold subreport parameters
                Map<String, Object> subReportParams = new HashMap<>();
                subReportParams.put("details", filteredDetails);

                // Fill the subreport with data
                JasperPrint subPrint = JasperFillManager.fillReport(subReport, subReportParams, dataSourceS);


                mainPrint.getPages().addAll(subPrint.getPages());

            }

            // Export the final report
            // Export to PDF
            exportToPdf(mainPrint, Filepath + "\\report.pdf");

            // Export to Excel (XLSX)
            exportToExcelXlsx(mainPrint, Filepath + "\\report.xlsx");

            // Export to Word
            exportToWord(mainPrint, Filepath + "\\report.docx");

            System.out.println("Report Generated....");
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
 * This method filters the list of employee details based on the provided employee id.
 *
 * @param employeeId The id of the employee whose details need to be fetched.
 * @param allEmployeeDetails The list of all employee details.
 * @return A list of employee details that belong to the specified employee id.
 */
public static List<EmployeeDetails> getEmployeeDetails(int employeeId, List<EmployeeDetails> allEmployeeDetails) {
    List<EmployeeDetails> filteredEmployeeDetails = new ArrayList<>();

    // Iterate through each employee detail and add it to the filtered list if it belongs to the specified employee id.
    for (EmployeeDetails employeeDetails : allEmployeeDetails) {
        if (employeeDetails.getEmployeeId() == employeeId) {
            filteredEmployeeDetails.add(employeeDetails);
        }
    }

    return filteredEmployeeDetails;
}


/**
 * This method calculates the total salary for each department in the list of employees.
 *
 * @param employees The list of employees.
 * @return A map where the keys are the department names and the values are the total salaries for each department.
 */
public static Map<String, Double> calculateTotalSalaryByDepartment(List<Employee> employees) {
    Map<String, Double> totalSalaryByDepartment = new HashMap<>();

    // Iterate through each employee and add their salary to the total salary for their department.
    for (Employee employee : employees) {
        String department = employee.getDepartment();
        double salary = employee.getSalary();

        // If the department already exists in the map, add the salary to the existing total salary.
        if (totalSalaryByDepartment.containsKey(department)) {
            double totalSalary = totalSalaryByDepartment.get(department);
            totalSalary += salary;
            totalSalaryByDepartment.put(department, totalSalary);
        } else {
            // If the department does not exist in the map, add it with the employee's salary as the total salary.
            totalSalaryByDepartment.put(department, salary);
        }
    }

    return totalSalaryByDepartment;
}


    /**
 * This method calculates the average salary for each department in the list of employees.
 *
 * @param employees The list of employees.
 * @return A map where the keys are the department names and the values are the average salaries for each department.
 */
private static Map<String, Double> calculateAverageSalaryByDepartment(List<Employee> employees) {
    // Group employees by department and calculate average salary for each department
    Map<String, List<Employee>> employeesByDepartment = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));

    Map<String, Double> averageSalaryByDepartment = new HashMap<>();
    for (Map.Entry<String, List<Employee>> entry : employeesByDepartment.entrySet()) {
        String department = entry.getKey();
        List<Employee> departmentEmployees = entry.getValue();
        double totalSalary = departmentEmployees.stream().mapToDouble(Employee::getSalary).sum();
        double averageSalary = totalSalary / departmentEmployees.size();
        averageSalaryByDepartment.put(department, averageSalary);
    }
    return averageSalaryByDepartment;
}

    public static void exportToPdf(JasperPrint jasperPrint, String outputFile) throws Exception {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFile)));
        exporter.exportReport();
    }

    public static void exportToExcel(JasperPrint jasperPrint, String outputFile) throws Exception {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFile)));
        exporter.exportReport();
    }

    public static void exportToExcelXlsx(JasperPrint jasperPrint, String outputFile) throws Exception {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFile)));
        exporter.exportReport();
    }

    public static void exportToWord(JasperPrint jasperPrint, String outputFile) throws Exception {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFile)));
        exporter.exportReport();
    }

}