package com.altersis.skillmatrix.assessment;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.skill.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
   /* List<Assessment> findByEmployeeId(Long employeeId); */
   /* List<Assessment> findByAssessmentDateAndEmployeeId(LocalDate date, Long idEmployee);
*/
    List<Assessment> findByAssessmentDate(Date assessmentDate);

    List<Assessment> findByEmployeeIdEmployee(Long idEmployee);

    List<Assessment> findByAssessmentDateAndEmployeeIdEmployee(Date date, Long idEmployee);

    List<Assessment> findBySkillIdSkill(Long idSkill);

    List<Assessment> findByEmployeeIdEmployeeAndAssessmentDateBetween(Long employeeId, Date startDate, Date endDate);

 List<Assessment> findByEmployeeIdEmployeeAndSkillSkillNameAndAssessmentDateBetween(Long employeeId, String skillName, Date startDate, Date endDate);
 List<Assessment> findByEmployeeIdEmployeeAndSkillIdSkill(Long idEmployee, Long idSkill);
 @Query("SELECT a FROM Assessment a WHERE a.employee = :employee AND a.skill = :skill ORDER BY a.assessmentDate DESC")
 List<Assessment> findAllByEmployeeAndSkillOrderByDateDesc(@Param("employee") Employee employee, @Param("skill") Skill skill);

}
