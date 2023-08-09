package com.altersis.skillmatrix.category;

import com.altersis.skillmatrix.skill.Skill;
//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor  @Data
public class Category {
    protected Category() {
    }

    public Category(String categoryName) {
        Objects.requireNonNull(categoryName);
        this.categoryName = categoryName;
    }
    @Id
    @GeneratedValue
    private Long idCategory;
    @Column(unique = true, nullable = false)
    private String categoryName;
    //private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    public List<Skill> getSkill() {
        return skills;
    }
    /*
    @OneToMany(mappedBy = "category", cascade =  CascadeType.ALL )
    private List<Skill> skills;
    public Category(String categoryName, String description, double scoreCategory) {
        this.categoryName = categoryName;
        this.description = description;
        this.scoreCategory = scoreCategory;
    }

     */
}
