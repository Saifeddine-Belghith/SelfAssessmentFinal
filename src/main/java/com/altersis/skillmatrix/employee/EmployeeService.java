package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.configuration.LoginDTO;
import com.altersis.skillmatrix.configuration.LoginMessage;
import com.altersis.skillmatrix.enumeration.Experience;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
/*
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
*/

import java.util.List;

public interface EmployeeService /* extends UserDetailsService */{
    LoginMessage loginEmployee(LoginDTO loginDTO);
    void assignCoach(Long coacheeId, Long coachId);

  //  Employee Login(String email, String password);

    List<EmployeeDTO> findAllEmployees();
//   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

  Employee findEmployeeByEmail(String email);


    Employee findById(Long id);

   /* EmployeeDTO save(EmployeeDTO employeeDTO);*/

   // @JsonIgnoreProperties(value = {"assessments"})
   // EmployeeDTO createEmployee(EmployeeDTO employeeDTO, HttpServletRequest request) throws IOException, FileUploadException;

    void deleteById(Long id);
    @JsonIgnoreProperties(value = {"assessments"}, allowSetters = true)
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    //EmployeeDTO createEmployee(EmployeeDTO employeeDTO, MultipartHttpServletRequest request) throws IOException, FileUploadException;

   // EmployeeDTO updateEmployee(Long id, EmployeeDTO employee);
   public EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployee);
    List<EmployeeDTO> findEmployeesByExperience(Experience experienceLevel);


//    List<Employee> getAllEmployeesByManagerId(Long managerId);


    /*  @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Optional<Employee> employee = employeeRepository.findEmployeeByEmail(email);
            if (employee == null) {
                throw new UsernameNotFoundException("Invalid email or password.");
            }
            return new User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
        }
    */
   // Employee login(String email, String password);

    //  Employee mapDTOToEmployee(EmployeeDTO employeeDTO) throws IOException;
  Employee mapDTOToEmployee(EmployeeDTO employeeDTO);

    Employee mapDTOToEmployeeForAssessment(EmployeeDTO employeeDTO);

    //Employee findByUsername(String username);

    Employee getMyProfile(String email);
    List<Employee> getOtherEmployees(Long idEmployee);
    List<EmployeeDTO> searchConsultantsBySkillsAndRatings(List<String> skills, int ratings);
    // EmployeeDTO createEmployeeWithPicture(EmployeeDTO employeeDTO, HttpServletRequest request) throws IOException, FileUploadException;
}
