package com.altersis.skillmatrix.assessment;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeService;
import com.altersis.skillmatrix.enumeration.Experience;
import com.altersis.skillmatrix.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {




    private final AssessmentService assessmentService;

    private final EmployeeService employeeService;
    private final SkillService skillService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService, EmployeeService employeeService, SkillService skillService ) {
        this.assessmentService = assessmentService;
        this.employeeService=employeeService;
        this.skillService=skillService;
    }

    //Get All Assessments
    @GetMapping("/all/{employeeId}")
    public ResponseEntity<List<AssessmentDTO>> getAllAssessments(@PathVariable Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if (employee.getIsCoach() == true || employee.getIsManager()==true) {
            List<AssessmentDTO> assessments = assessmentService.getAllAssessments();
            return ResponseEntity.ok(assessments);

    } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all/{employeeId}/{employeeId2}")
    public ResponseEntity<List<AssessmentDTO>> getAllAssessments(@PathVariable Long employeeId , @PathVariable Long employeeId2) {
        Employee employee = employeeService.findById(employeeId);
        Employee employee2 = employeeService.findById(employeeId2);
        if ((employee.getIsCoach() == true || employee.getIsManager()==true) && employee2.getIsCoachee()==true)  {
            List<AssessmentDTO> assessments = assessmentService.findByEmployeeId(employeeId2);
            return ResponseEntity.ok(assessments);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Get Assessment By Id
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentDTO> getAssessmentById(@PathVariable Long id) {
        AssessmentDTO assessment = assessmentService.getAssessmentById(id);
        if (assessment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assessment);
    }

    //Create a new assessment
    @PostMapping("/create")
    public AssessmentDTO save(@RequestBody AssessmentDTO assessmentDTO) {
        AssessmentDTO createdAssessment = assessmentService.createAssessment(assessmentDTO);
        return createdAssessment;
    }
    @PostMapping("/saveAssessments")
    public ResponseEntity<?> saveAssessments(@RequestBody EmployeeAssessmentDTO employeeAssessmentDTO) {

        try {
            Long idEmployee = employeeAssessmentDTO.getIdEmployee();
            for (AssessmentDTO assessmentDTO : employeeAssessmentDTO.getAssessments()) {
                Long skillId = assessmentDTO.getIdSkill();
                Integer rating = assessmentDTO.getRating();
             //   String comment = assessmentDTO.getComment();
                Assessment assessment = new Assessment();
                assessment.setEmployee(employeeService.findById(idEmployee));
                assessment.setSkill(skillService.toEntity(skillService.viewSkillById(skillId)));
                assessment.setRating(rating);
             //   assessment.setComment(comment);
                assessment.setAssessmentDate(new Date());
                assessmentService.saveAssessment(assessment);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving assessments.");
        }
    }



    //Get Assessment by employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsByEmployeeId(@PathVariable Long employeeId) {
        List<AssessmentDTO> assessments = assessmentService.findByEmployeeId(employeeId);
            if (assessments.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

        return ResponseEntity.ok(assessments);
    }

    //Get assessment by Skill
@GetMapping("/skill/{skillId}")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsBySkillId(@PathVariable Long skillId) {
        List<AssessmentDTO> assessments = assessmentService.findBySkillId(skillId);
        if (assessments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assessments);
    }

    //Get All assessment by date (i think it's not necessary but i implement it)
    @GetMapping("/date/{assessmentDate}")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsByDate(@PathVariable String assessmentDate) throws ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(assessmentDate);
            List<AssessmentDTO> assessments = assessmentService.findByAssessmentDate(date);

            if (assessments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(assessments);
        }

    }

    //Get all the assessment for an employee in a given date
    @GetMapping("/employee/{idEmployee}/date/{assessmentDate}")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsByIdEmployeeAndDate(
            @PathVariable Long idEmployee,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date assessmentDate) {
        List<AssessmentDTO> assessments = assessmentService.findByAssessmentDateAndIdEmployee(assessmentDate, idEmployee);
        if (assessments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assessments);
    }


    //Update assessment (it's not necessary but maybe we will need it in the future)
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentDTO> updateAssessment(@PathVariable Long id, @RequestBody AssessmentDTO assessmentDTO) {
        if (assessmentDTO.getIdAssessment() == null || !assessmentDTO.getIdAssessment().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        AssessmentDTO updatedAssessment = assessmentService.updateAssessment(id, assessmentDTO);
        if (updatedAssessment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAssessment);
    }

    //Get rating changes for specific employee and specific Skill over a time period (startDate/endDate)
    @GetMapping("/employee/{employeeId}/skill/{skillName}")
    public ResponseEntity<List<Map<String, Object>>> getRatingChangesForEmployeeAndSkill(
            @PathVariable Long employeeId,
            @PathVariable String skillName,
            @RequestParam ("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam ("endDate") @DateTimeFormat(pattern="yyyy-MM-dd")Date endDate) {

        List<Map<String, Object>> ratingChanges = assessmentService.getRatingChangesForEmployeeAndSkill(employeeId, skillName, startDate, endDate);

        if (ratingChanges.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ratingChanges);
        }
    }

    //Get last assessments for an employee in each skill
    @GetMapping("/employees/{employeeId}/last-assessments")
    public ResponseEntity<List<AssessmentDTO>> getLastAssessmentsByEmployee(@PathVariable Long employeeId) {
        List<AssessmentDTO> lastAssessments = assessmentService.getLastAssessmentsByEmployee(employeeId);
        return ResponseEntity.ok(lastAssessments);
    }


    //Calculate average rating for each Category for specific Employee
    @GetMapping("/average-ratings/employee/{idEmployee}")
    public ResponseEntity <Map<Long, Double>> calculateAverageRatingsByCategoryAndEmployee(@PathVariable Long idEmployee) {
        Map<Long, Double> averageRatingsByEmployeeAndCategory = assessmentService.calculateAverageRatingsByCategoryAndEmployee(idEmployee);
        return ResponseEntity.ok(averageRatingsByEmployeeAndCategory);
    }

    //Calculate average rating for each category for a given Experience Level
    @GetMapping("/average-ratings/experience/{experienceLevel}")
    public ResponseEntity <Map<Long, Double>> calculateAverageRatingsByCategoryAndExperience(@PathVariable Experience experienceLevel) {
        Map<Long, Double> averageRatingsByEmployeeAndCategory = assessmentService.calculateAverageRatingsByCategoryAndExperience(experienceLevel);
        return ResponseEntity.ok(averageRatingsByEmployeeAndCategory);
    }

    @GetMapping("/assistance/skill/{idSkill}/{idEmployee}")
    public ResponseEntity<Map<Long, Double>> getAssistanceByEmployeeAndCategory(@PathVariable Long idSkill, @PathVariable Long idEmployee) {
        Map<Long, Double> assistanceByEmployeeAndCategory = assessmentService.getAssistance(idSkill, idEmployee);
        return ResponseEntity.ok(assistanceByEmployeeAndCategory);
    }
}
