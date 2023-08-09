package com.altersis.skillmatrix.skill;
/*
import com.altersis.skills.category.entities.Category;
import com.altersis.skills.category.repositories.CategoryRepository;*/
import com.altersis.skillmatrix.category.CategoryRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*import jakarta.validation.Valid;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/skill")
public class SkillController {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    public SkillController(SkillRepository skillRepository, CategoryRepository categoryRepository) {
        this.skillRepository = skillRepository;
        this.categoryRepository = categoryRepository;
    }

    //Create Skill
    @PostMapping("/create/{catid}")/* WE WILL ADD THIS "/{catid}" to the Path AFTER CREATING CATEGORIES */
    @ResponseBody
    public ResponseEntity<SkillDTO> createSkill(@RequestBody SkillDTO skill, @PathVariable Long catid) {
        SkillDTO createSkill = skillService.createSkill(skill, catid);
        return new ResponseEntity<SkillDTO>(createSkill, HttpStatus.CREATED);
    }
    //view List Skills
    @JsonIgnoreProperties(value = {"category"}, allowSetters = true)
    @GetMapping("/viewAll")
    public ResponseEntity<List<SkillDTO>>viewAllSkill(){
        List<SkillDTO> viewAll = skillService.viewAll();
        return new ResponseEntity<List<SkillDTO>>(viewAll, HttpStatus.OK);
    }

    //view Skill by id
    @GetMapping("/view/{id}")
    public ResponseEntity<SkillDTO> viewSkillById(@PathVariable("id") Long id){
        SkillDTO viewSkillById = skillService.viewSkillById(id);
        return new ResponseEntity<SkillDTO>(viewSkillById, HttpStatus.OK);
    }

    //delete Skill by id
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteSkillById(@PathVariable("id") Long id){
        skillService.deleteSkillById(id);
        return new ResponseEntity<String>("Skill deleted successfully", HttpStatus.ACCEPTED);
    }

    //Update skill without updating the Note field
    @PutMapping("/update/{id}")
    public ResponseEntity<SkillDTO> updateSkill(@PathVariable("id") Long id, @RequestBody SkillDTO updatedSkill){
        SkillDTO updateSkill = skillService.updateSkill(id, updatedSkill);
        return new ResponseEntity<SkillDTO>(updateSkill, HttpStatus.ACCEPTED);
    }


    //Get all skills
   /* @GetMapping("/allSkills")
    public ResponseEntity<List<Skill>> getAllSkills(@RequestParam(required = false) String name) {
        try {
            List<Skill> skills = new ArrayList<Skill>();

            if (name == null)
                skillRepository.findAll().forEach(Skill -> skills.add(Skill)) ;
            else
                skillRepository.findBySkillName(name).forEach(Skill -> skills.add((com.altersis.skills.skill.entities.Skill) Skill));
            if (skills.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skills, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
    //Get skill by id
    @GetMapping("/skill/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) {
        Optional<Skill> skillData = skillRepository.findById(id);
        if (skillData.isPresent()) {
            return new ResponseEntity<>(skillData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Create Skill



    @PostMapping("/createSkill")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        try {
            Category category = new Category(skill.getCategory().getCategoryName(), skill.getCategory().getDescription(), skill.getCategory().getScoreCategory());
            Skill _skill = skillRepository
                    .save(new Skill(skill.getSkillName(), skill.getDescription(), category  ));
            return new ResponseEntity<>(_skill, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.valueOf(500));
        }
    }


    //Update Skill
    @PutMapping("/update/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable("id") Long id, @RequestBody Skill skill) {
        Optional<Skill> skillData = skillRepository.findById(id);
        if (skillData.isPresent()) {
            Skill _skill = skillData.get();
            _skill.setSkillName(skill.getSkillName());
            _skill.setDescription(skill.getDescription());
            return new ResponseEntity<>(skillRepository.save(_skill), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete Skill
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteSkill(@PathVariable("id") Long id) {
        try {
            skillRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete all skills
    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllSkills() {
        try {
            skillRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/*
    //Get all skills by category
    @GetMapping("/categories/{categoryId}/skills")
    public ResponseEntity<List<Skill>> getAllSkillsByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("No category with id: " + categoryId);
        }
        List<Skill> skills = skillRepository.findByCategory_IdCategory(categoryId);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    //Create skill by category
    @PostMapping("/categories/{categoryId}/create/skills")
    public ResponseEntity<Skill> createSkillByCategory(@PathVariable(value = "categoryId") Long categoryId,
                                                       @RequestBody Skill skillRequest) {
        Skill skill = categoryRepository.findById(categoryId)
                .map(category -> {
                    skillRequest.setCategory(category);
                    return skillRepository.save(skillRequest);
                }).orElseThrow(() -> new ResourceNotFoundException("No category with id: " + categoryId));
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }


/*
    //Delete all skills of category
    @DeleteMapping("/categories/{categoryId}/delete/skills")
    public ResponseEntity<List<Skill>> deleteAllSkillsOfCategory(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("No category with id: " + categoryId);
        }
        skillRepository.deleteByCategory_IdCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    */

    //The New Methods



    //find Skill by Category
    @GetMapping("/category/{catId}")
    public ResponseEntity<List<SkillDTO>> getSkillByCategory(@PathVariable("catId") Long catId){
        List<SkillDTO> skillByCategory = skillService.findSkillByCategory(catId);
        return new ResponseEntity<List<SkillDTO>>(skillByCategory, HttpStatus.ACCEPTED);
    }
/*
    //update Skill Note
    @PutMapping("/skills/{id}/note")
    public ResponseEntity<SkillDTO> updateSkillNoteById(@PathVariable("id") Long id, @RequestBody SkillDTO updatedSkillNote) {
        SkillDTO skill = skillService.viewSkillById(id);
        if (skill == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       /* int note = updatedSkillNote.getNote();
        if (note < 0 || note > 3) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        skill.setNote(note);
        SkillDTO updateSkillNote = skillService.updateSkillNote(id,updatedSkillNote);
        return new ResponseEntity<SkillDTO>(updateSkillNote, HttpStatus.ACCEPTED);
    }
    */
}