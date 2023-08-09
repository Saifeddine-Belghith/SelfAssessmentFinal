package com.altersis.skillmatrix.configuration;

import com.altersis.skillmatrix.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<LoginMessage> loginEmployee(@RequestBody LoginDTO loginDTO) {
        LoginMessage loginMessage = employeeService.loginEmployee(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginMessage> login(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password, HttpSession session) {
//        Employee employee = employeeService.Login(email, password);
//        LoginDTO loginDTO = new LoginDTO(email, password);
//        LoginMessage loginMessage = employeeService.loginEmployee(loginDTO);
//        if (employee != null) {
//            session.setAttribute("employee", employee);
//            return ResponseEntity.ok(loginMessage);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }



}
