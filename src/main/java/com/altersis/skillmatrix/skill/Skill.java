package com.altersis.skillmatrix.skill;

import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.category.Category;
//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
 @Data @AllArgsConstructor @NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSkill;
    @Column(name = "skill_name", unique = true)
    private String skillName;
    private String description;
    @OneToMany(mappedBy = "skill")
    private List<Assessment> assessments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
