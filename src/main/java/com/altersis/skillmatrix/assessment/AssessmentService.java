package com.altersis.skillmatrix.assessment;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.Experience;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AssessmentService {
    List<AssessmentDTO> getAllAssessments();
    AssessmentDTO getAssessmentById(Long id);
    AssessmentDTO createAssessment(AssessmentDTO assessmentDTO);
    AssessmentDTO updateAssessment(Long id, AssessmentDTO assessmentDTO);

    List<AssessmentDTO> findByAssessmentDateAndIdEmployee(Date date, Long idEmployee);

    List<AssessmentDTO> findByAssessmentDate(Date assessmentDate);

    List<AssessmentDTO> findByEmployeeId(Long idEmployee);
    List<AssessmentDTO> findBySkillId(Long idSkill);

    void deleteAssessment(Long id);

  /*  Map<String, List<Integer>> getRatingChangesForEmployee(Long employeeId, LocalDate startDate, LocalDate endDate); */

    List<Map<String, Object>> getRatingChangesForEmployeeAndSkill(Long employeeId, String skillName, Date startDate, Date endDate);

    void saveAssessment(Assessment assessment);
     Double getAverageSkillLevelForSkill(Long idSkill);
    List<Assessment> findByEmployeeIdEmployeeAndSkillIdSkill(Long idEmployee, Long idSkill);

//    void saveAssessment(EmployeeAssessmentDTO employeeAssessmentDTO);

    // Last Assessment for each skill by employee
    List<AssessmentDTO> getLastAssessmentsByEmployee(Long employeeId);
//    List<Assessment> findByEmployeeIdEmployee( Long idEmployee);

    //Calculate average rating for each Category for specific Employee
    public Map<Long, Double> calculateAverageRatingsByCategoryAndEmployee(Long idEmployee);

    //Calculate average rating for each category for a given Experience Level
    public Map<Long, Double> calculateAverageRatingsByCategoryAndExperience(Experience experienceLevel);

    //Assistance
    Map<Long, Double> getAssistance(Long idSkill, Long idEmployee);

    AssessmentDTO mapAssessmentToDTO(Assessment assessment);

    //  void saveAssessment(EmployeeAssessmentDTO employeeAssessmentDTO);
    /*  List<AssessmentDTO> findByAssessmentDateAndEmployeeId(LocalDate date, Long idEmployee);
*/
}

