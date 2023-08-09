package com.altersis.skillmatrix.reminder;

import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.assessment.AssessmentService;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;


@Service
@Component
@EnableScheduling
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AssessmentService assessmentService;

    @Scheduled(cron="0 0 0 1 */6 *") // Run at 00:00:00 AM every 6 months
 //   @Scheduled(cron="0 */2 * ? * *") // Run every 2 minutes (for testing)
    public void remindEmployeesToFillSkillMatrix() {
        System.out.println("Hello from remindEmployeesToFillSkillMatrix methode");
        SimpleMailMessage message = new SimpleMailMessage();
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        for (EmployeeDTO employee : employees) {
            boolean firstSubmitted = false;
            boolean secondSubmitted = false;
            if (employee != null) {
                List<AssessmentDTO> assessments = assessmentService.findByEmployeeId(employee.getIdEmployee());
                if (assessments != null) {
                    System.out.println("Assessments is not null for person with this email " + employee.getEmail());
                    Calendar now = Calendar.getInstance();
                    // Set the calendar instance to the first day of January of the current year
                    now.set(Calendar.MONTH, Calendar.JANUARY);
                    now.set(Calendar.DAY_OF_MONTH, 1);
                    int year = now.get(Calendar.YEAR);
                    for (AssessmentDTO assessment : assessments) {
                        Calendar assessmentDate = Calendar.getInstance();
                        assessmentDate.setTime(assessment.getAssessmentDate());
                        if (assessmentDate.get(Calendar.YEAR) == year
                                && assessmentDate.get(Calendar.MONTH) >= Calendar.JANUARY
                                && assessmentDate.get(Calendar.MONTH) <= Calendar.JUNE) {
                            System.out.println("Assessment submitted by " + employee.getEmail() + " in the first half of the year");
                            firstSubmitted = true;
                            break;
                        }
                        if (assessmentDate.get(Calendar.YEAR) == year
                                && assessmentDate.get(Calendar.MONTH) >= Calendar.JULY
                                && assessmentDate.get(Calendar.MONTH) <= Calendar.DECEMBER) {
                            System.out.println("Assessment submitted by " + employee.getEmail() + " in the second half of the year");
                            secondSubmitted = true;
                            break;
                        }
                    }
                }
                if (!firstSubmitted) {

                    String employeeEmail = employee.getEmail();
                    System.out.println("Person with this email  " + employee.getEmail() + " doesn't submit his assessment");
                    String subject = "Reminder: Fill in Skills Matrix";
                    String body = "Dear " + employee.getFirstName() + ",\n\nPlease submit your skills matrix for the first half of the year. You can do so by logging in to the skills matrix system at [10.66.12.54:4200].\n\n";
                    message.setTo(employeeEmail);
                    message.setSubject(subject);
                    message.setText(body);
                    mailSender.send(message);
                }
                if (!secondSubmitted) {

                    String employeeEmail = employee.getEmail();
                    System.out.println("Person with this email  " + employee.getEmail() + " doesn't submit his assessment");
                    String subject = "Reminder: Fill in Skills Matrix";
                    String body = "Dear " + employee.getFirstName() + ",\n\nPlease submit your skills matrix for the second half of the year. You can do so by logging in to the skills matrix system at [10.66.12.54:4200].\n\n";
                    message.setTo(employeeEmail);
                    message.setSubject(subject);
                    message.setText(body);
                    mailSender.send(message);
                }


            }
        }

    }
}
