package com.altersis.skillmatrix.employee;

public class EmployeeNotFoundException extends RuntimeException {
    private String message;
    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id " + id);
    }
    public EmployeeNotFoundException(String email) {
        this.message = "Employee not found with email " + email;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

