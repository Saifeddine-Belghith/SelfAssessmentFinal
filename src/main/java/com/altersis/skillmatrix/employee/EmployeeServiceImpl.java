package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.assessment.AssessmentRepository;
import com.altersis.skillmatrix.configuration.CustomDecoder;
import com.altersis.skillmatrix.configuration.LoginDTO;
import com.altersis.skillmatrix.configuration.LoginMessage;
import com.altersis.skillmatrix.enumeration.Experience;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*import org.apache.tomcat.util.http.fileupload.FileItemFactory;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomDecoder passwordDecoder;


    private final AssessmentRepository assessmentRepository;

    @Lazy
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CustomDecoder passwordDecoder, PasswordEncoder passwordEncoder, AssessmentRepository assessmentRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordDecoder=passwordDecoder;
        this.passwordEncoder = passwordEncoder;
        this.assessmentRepository=assessmentRepository;
    }


//    @Override
//    public Employee Login(String email, String password) {
//        Employee employee = employeeRepository.findByEmail(email);
//        if (employee != null && employee.getPassword().equals(password)) {
//            return employee;
//        }
//        return null;
//    }
    @Override
    public LoginMessage loginEmployee(LoginDTO loginDTO) {
        String msg = "";
        Employee employee1 = employeeRepository.findByEmail(loginDTO.getEmail());
        if (employee1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = employee1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            // Logging the result
            if (isPwdRight) {
                System.out.println("Password matched!");
            } else {
                System.out.println("Password does not match!");
            }
            if (isPwdRight) {
                Optional<Employee> employee = employeeRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginMessage("Login Success", true, employee.get().getIdEmployee());
                } else {
                    return new LoginMessage("Login Failed", false, null);
                }
            } else {

                return new LoginMessage("password Not Match", false, null);
            }
        }else {
            return new LoginMessage("Email does not exist", false, null);
        }


    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Employee employee = employeeRepository.findEmployeeByEmail(email);
//        if (employee == null) {
//            throw new UsernameNotFoundException("Invalid email or password.");
//        }
//        return new User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
//    }


    @Override
    public Employee getMyProfile(String email) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
              throw new EmployeeNotFoundException(email);
        }
        return employee;
    }

    @Override
    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> mapEmployeeToDTO(employee))
                .collect(Collectors.toList());
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

   /* @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }*/

    @Override
    public Employee findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        return (employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        employee.setPhone(updatedEmployee.getPhone());
        employee.setExperienceLevel(updatedEmployee.getExperienceLevel());
        employee.setRole(updatedEmployee.getRole());
        employee.setIsCoach(updatedEmployee.getIsCoach());
        employee.setIsCoachee(updatedEmployee.getIsCoachee());
        employee.setIsManager(updatedEmployee.getIsManager());

        Employee updated = employeeRepository.save(employee);
        return mapEmployeeToDTO(updated);
    }

    @Override
    @JsonIgnoreProperties(value = {"assessments" , "setCoach"}, allowSetters = true)
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO){
        Employee employee = mapDTOToEmployeeForNewEmployee(employeeDTO);
        Employee createdemployee = employeeRepository.save(employee);

        return mapEmployeeToDTO(createdemployee);
    }

   @Override
    public void deleteById(Long id) {
        List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployee(id);
        assessmentRepository.deleteAll(assessments);
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDTO> findEmployeesByExperience(Experience experienceLevel) {
        List<Employee> employees = employeeRepository.findEmployeeByExperienceLevel(experienceLevel);
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDTOs.add(mapEmployeeToDTO(employee));
        }
        return employeeDTOs;
    }

    public void assignCoach(Long coacheeId, Long coachId) {
        Employee coachee = employeeRepository.findById(coacheeId)
                .orElseThrow(() -> new ResourceNotFoundException("Coachee not found with id " + coacheeId));

        Employee coach = employeeRepository.findById(coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id " + coachId));

        coachee.setCoach(coach);

        employeeRepository.save(coachee);

    }
    public void assignManager(Long employeeId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + managerId));

        employee.setManager(manager);

        employeeRepository.save(employee);
    }
    @Override
    public List<Employee> getOtherEmployees(Long idEmployee) {
        Employee employee = employeeRepository.findById(idEmployee).orElse(null);
        if (employee != null) {
            return employeeRepository.findOtherEmployeesByIdEmployee(idEmployee);
        } else {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a manager.");
        }
    }
    public List<EmployeeDTO> searchConsultantsBySkillsAndRatings(List<String> skills, int ratings) {
        List<Employee> consultants = employeeRepository.findConsultantsBySkillsAndRatings(skills, ratings);

        // Convert the list of consultants to DTOs
        List<EmployeeDTO> consultantDTOs = consultants.stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());

        return consultantDTOs;
    }





   /* @Override
    public Employee login(String email, String password) {
        Employee optionalEmployee = employeeRepository.findEmployeeByEmail(email);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            System.out.println("Found employee with email: " + email);
            if (employee.getPassword().equals(password)) {
                System.out.println("Password is correct for employee with email: " + email);
                return employee;
            }
            else {
                System.out.println("Incorrect password for employee with email: " + email);
            }
        } else {
            System.out.println("No employee found with email: " + email);

        }
        return null;
    }
*/

    private EmployeeDTO mapEmployeeToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setIdEmployee(employee.getIdEmployee());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setExperienceLevel(employee.getExperienceLevel());
        employeeDTO.setIsCoach(employee.getIsCoach());
        employeeDTO.setIsCoachee(employee.getIsCoachee());
        employeeDTO.setIsManager(employee.getIsManager());
      /*  // Decode Base64 encoded picture string to byte[]
        if (employee.getPicture() != null) {
            // Decode base64 string to byte array
            byte[] pictureBytes = Base64.getDecoder().decode(employee.getPicture());
            employeeDTO.setPicture(pictureBytes);
        }*/
        employeeDTO.setRole(employee.getRole());
//        employeeDTO.setCoach(employeeDTO.getCoach());
    /*    employeeDTO.setAssessments(employee.getAssessments().stream()
                .map(assessment -> mapAssessmentToDTO(assessment))
                .collect(Collectors.toList()));*/
        return employeeDTO;
    }
   /* @Override
    public EmployeeDTO createEmployeeWithPicture(EmployeeDTO employeeDTO, HttpServletRequest request) throws IOException, FileUploadException {
        Employee employee = mapDTOToEmployee(employeeDTO); // pass employeeDTO here
        Employee savedEmployee = employeeRepository.save(employee);
        return mapEmployeeToDTO(savedEmployee);
    }
*/
    public Employee mapDTOToEmployee(EmployeeDTO employeeDTO)  {
        Employee employee = new Employee();
        employee.setIdEmployee(employeeDTO.getIdEmployee());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setPhone(employeeDTO.getPhone());
        employee.setExperienceLevel(employeeDTO.getExperienceLevel());
        employee.setRole(employeeDTO.getRole());
        employee.setIsCoach(employeeDTO.getIsCoach());
        employee.setIsCoachee(employeeDTO.getIsCoachee());
        employee.setIsManager(employeeDTO.getIsManager());

            Employee coachDto=new Employee();
            coachDto.setCoach (employee.getCoach());
            employee.setCoach(coachDto);




       /* if (request instanceof MultipartHttpServletRequest) {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("picture");
            if (file != null && !file.isEmpty()) {
                byte[] pictureBytes = file.getBytes();
                employee.setPicture(new String (pictureBytes));
            }*/

        // Convert byte[] picture to Base64 encoded string
   /*     if (employeeDTO.getPicture() != null) {
            // Encode byte array to base64 string
            String pictureString = Base64.getEncoder().encodeToString(employeeDTO.getPicture());
            employee.setPicture(pictureString);
        }
*/

//        List<Employee> newCoaches = employee.getCoaches();
//        if (newCoaches != null && !newCoaches.isEmpty()) {
//            employee.setCoaches(newCoaches);
//        } else {
//            employee.setCoaches(Collections.emptyList());
//        }


        return employee;
    }
    public Employee mapDTOToEmployeeForNewEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setIdEmployee(employeeDTO.getIdEmployee());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setPhone(employeeDTO.getPhone());
        employee.setExperienceLevel(employeeDTO.getExperienceLevel());
        employee.setRole(employeeDTO.getRole());
        employee.setIsCoach(employeeDTO.getIsCoach());
        employee.setIsCoachee(employeeDTO.getIsCoachee());
        employee.setIsManager(employeeDTO.getIsManager());

        return employee;
    }

    public Employee mapDTOToEmployeeForAssessment(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setIdEmployee(employeeDTO.getIdEmployee());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setPhone(employeeDTO.getPhone());
        employee.setExperienceLevel(employeeDTO.getExperienceLevel());
        employee.setRole(employeeDTO.getRole());
        employee.setIsCoach(employeeDTO.getIsCoach());
        employee.setIsCoachee(employeeDTO.getIsCoachee());
        employee.setIsManager(employeeDTO.getIsManager());
        return employee;
    }



        private AssessmentDTO mapAssessmentToDTO(Assessment assessment) {
        AssessmentDTO assessmentDTO = new AssessmentDTO();
        assessmentDTO.setIdAssessment(assessment.getIdAssessment());
        assessmentDTO.setIdEmployee(assessment.getEmployee().getIdEmployee());
        assessmentDTO.setIdSkill(assessment.getSkill().getIdSkill());
        assessmentDTO.setRating(assessment.getRating());
       // assessmentDTO.setComment(assessment.getComment());
        assessmentDTO.setAssessmentDate(assessment.getAssessmentDate());
        return assessmentDTO;
    }

}
