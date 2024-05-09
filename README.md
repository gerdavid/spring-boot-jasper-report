# Employee Application
This application is a Spring Boot application that generates reports using JasperReports. The reports are generated based on a list of employees and their details. The application allows you to apply filtering based on department and salary range, and then generate reports that include subreports for each employee's details.

## Getting Started
To run this application, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in your favorite IDE.
3. Run the `EmployeeApplication.java` file.

## Code Structure
The main method of the `EmployeeApplication` class is responsible for generating the reports. It first creates a list of employees with details and a list of employee details. Then, it applies filtering based on department and salary range, and compiles the main report and subreports. Finally, it exports the final report to PDF, Excel (XLSX), and Word formats.

## Report Generation Process
The report generation process involves the following steps:

1. Apply filtering based on department and salary range(Data source here is a hard coded Array List).
2. Compile the main report and subreports.
3. Fill the main report with data.
4. Iterate through each employee and fetch their details.
5. Compile a subreport for each employee's details.
6. Fill the main report with the subreports.
7. Export the final report to the desired format (PDF, XLSX, or DOCX).

## Code Snippets
Here are some code snippets that demonstrate the functionality of the application:

```java
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

// Compile the main report and subreports
JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(filteredEmployees);
JasperReport mainReport = JasperCompileManager.compileReport(Filepath + "\\EmployeeReport.jrxml");

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

// Export the final report to the desired format
exportToPdf(mainPrint, Filepath + "\\report.pdf");
exportToExcel(mainPrint, Filepath + "\\report.xlsx");
exportToWord(mainPrint, Filepath + "\\report.docx");

## NOTE: The sub report is generated at different pages after the main report due to the nature of the data source which is hardcoded Array List. 