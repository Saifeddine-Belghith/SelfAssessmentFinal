package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.enumeration.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//    List<Employee> getAllEmployeesByManagerId(Long managerId);

    Optional<Employee> findOneByEmailAndPassword(String email, String password);
    Employee findByEmail(String email);
    List<EmployeeDTO> findByIdEmployee(Long idEmployee);

    List<Employee> findEmployeeByExperienceLevel(Experience experienceLevel);
    List<Employee> findOtherEmployeesByIdEmployee(Long idEmployee);

    List<Employee> findEmployeesByRole(String Role);

    @Query("SELECT e FROM Employee e WHERE EXISTS " +
            "(SELECT a FROM Assessment a WHERE a.employee = e " +
            "AND a.skill.skillName IN :skills AND a.rating >= :ratings " +
            "AND a.assessmentDate = " +
            "(SELECT MAX(ass.assessmentDate) FROM Assessment ass WHERE ass.employee = e " +
            "AND ass.skill.skillName = a.skill.skillName))")
    List<Employee> findConsultantsBySkillsAndRatings(@Param("skills") List<String> skills, @Param("ratings") int ratings);

    Employee findEmployeeByEmail(String email);
    // Employee saveWithPicture(Employee employee, String pictureFilename);
}
