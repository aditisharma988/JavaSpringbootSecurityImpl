package com.security.securityImpl.util;

import com.security.securityImpl.entity.Employee;

import java.util.List;

public class CsvGeneratorUtil {

    private static final String CSV_HEADER ="Id,Name,Age,Designation,Salary\n";

    public String generateCsv(List<Employee> employeeList){

        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for(Employee employee:employeeList){
            csvContent.append(employee.getId()).append(",")
                    .append(employee.getAge()).append(",")
                    .append(employee.getName()).append(",")
            .append(employee.getDesignation()).append(",")
            .append(employee.getSalary()).append("\n");
//            csvContent.append(employee.getId()).append(",")
        }
        return csvContent.toString();

    }
}
