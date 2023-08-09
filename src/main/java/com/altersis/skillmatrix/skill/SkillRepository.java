package com.altersis.skillmatrix.skill;

import com.altersis.skillmatrix.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByCategory(Category cat);
    Skill findBySkillName(String skillName);
    @Query("SELECT s FROM Skill s WHERE s.skillName = :skillName")
    List<Skill> findSkillBySkillName(@Param("skillName") String skillName);

}
