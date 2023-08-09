package com.altersis.skillmatrix.assessment;

import com.altersis.skillmatrix.category.CategoryDTO;
import com.altersis.skillmatrix.category.CategoryService;
import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.employee.EmployeeService;
import com.altersis.skillmatrix.enumeration.Experience;
import com.altersis.skillmatrix.skill.SkillDTO;
import com.altersis.skillmatrix.skill.SkillRepository;
import com.altersis.skillmatrix.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional

public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentRepository assessmentRepository;
//    private final AssessmentService assessmentService;

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    @Lazy
    private final EmployeeService employeeService;
    private final SkillService skillService;
    private final CategoryService categoryService;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository,
                                 EmployeeRepository employeeRepository,
                                 SkillRepository skillRepository,
                                 @Lazy EmployeeService employeeService,
                                 SkillService skillService,
                                 CategoryService categoryService) {
        this.assessmentRepository = assessmentRepository;
      this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.employeeService = employeeService;
        this.skillService = skillService;
        this.categoryService = categoryService;
//        this.assessmentService= assessmentService;
    }
    @Override
    public List<AssessmentDTO> getAllAssessments() {
        List<Assessment> assessments = assessmentRepository.findAll();
        return assessments.stream()
                .map(this::mapAssessmentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssessmentDTO getAssessmentById(Long id) {
        Assessment assessment = assessmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assessment not found"));
        return mapAssessmentToDTO(assessment);
    }

    @Override
    public AssessmentDTO createAssessment(AssessmentDTO assessmentDTO) {

        Assessment assessment = mapDTOToAssessment(assessmentDTO);
        assessmentDTO.setAssessmentDate(assessmentDTO.getAssessmentDate());
        Assessment savedAssessment = assessmentRepository.save(assessment);
        return mapAssessmentToDTO(savedAssessment);
    }

    @Override
    public AssessmentDTO updateAssessment(Long id, AssessmentDTO assessmentDTO) {
        Assessment assessment = assessmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assessment not found"));
        assessment.setEmployee(employeeService.findById(assessmentDTO.getIdEmployee()));
        assessment.setSkill(skillRepository.findById(assessmentDTO.getIdSkill()).orElseThrow(() -> new RuntimeException("Skill not found")));
        assessment.setRating(assessmentDTO.getRating());
        assessment.setAssessmentDate(assessmentDTO.getAssessmentDate());
        Assessment updatedAssessment = assessmentRepository.save(assessment);
        return mapAssessmentToDTO(updatedAssessment);
    }
    @Override
    public List<AssessmentDTO> findByAssessmentDateAndIdEmployee(Date date, Long idEmployee) {
        List<Assessment> assessments = assessmentRepository.findByAssessmentDateAndEmployeeIdEmployee(date, idEmployee);
        return assessments.stream()
                .map(assessment -> mapAssessmentToDTO(assessment))
                .collect(Collectors.toList());
    }
    @Override
    public List<AssessmentDTO> findByAssessmentDate(Date date) {
        List<Assessment> assessments = assessmentRepository.findByAssessmentDate(date);
        return assessments.stream()
                .map(assessment -> mapAssessmentToDTO(assessment))
                .collect(Collectors.toList());
    }

    @Override
    public List<AssessmentDTO> findByEmployeeId(Long idEmployee) {
        List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployee(idEmployee);
        return assessments.stream()
                .map(this::mapAssessmentToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<AssessmentDTO> findBySkillId(Long idSkill) {
        List<Assessment> assessments = assessmentRepository.findBySkillIdSkill(idSkill);
        return assessments.stream()
                .map(this::mapAssessmentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getRatingChangesForEmployeeAndSkill(Long employeeId, String skillName, Date startDate, Date endDate) {
        // Get all assessments for the specified employee and skill during the specified time period
        List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployeeAndSkillSkillNameAndAssessmentDateBetween(employeeId, skillName, startDate, endDate);

        // Extract ratings and dates from assessments
        List<Map<String, Object>> ratingChanges = new ArrayList<>();
        for (Assessment assessment : assessments) {
            Map<String, Object> ratingChange = new HashMap<>();
            ratingChange.put("rating", assessment.getRating());
            ratingChange.put("assessmentDate", assessment.getAssessmentDate());
            ratingChanges.add(ratingChange);
        }

        return ratingChanges;
    }


    // Return result as a map


    @Override
    public void saveAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    @Override
    public Double getAverageSkillLevelForSkill(Long idSkill) {
        List<Assessment> assessments = assessmentRepository.findBySkillIdSkill(idSkill);
        if (assessments.isEmpty()) {
            return null;
        }
        double totalRating = 0;
        for (Assessment assessment : assessments) {
            totalRating += assessment.getRating();
        }
        return totalRating / assessments.size();
    }

    @Override
    public List<Assessment> findByEmployeeIdEmployeeAndSkillIdSkill(Long idEmployee, Long idSkill) {
        return assessmentRepository.findByEmployeeIdEmployeeAndSkillIdSkill(idEmployee, idSkill);
    }

    @Override
    public List<AssessmentDTO> getLastAssessmentsByEmployee(Long employeeId) {
        List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployee(employeeId);
        Map<Long, Assessment> lastAssessmentsMap = new HashMap<>();

        for (Assessment assessment : assessments) {
            Long skillId = assessment.getSkill().getIdSkill();
            Date assessmentDate = assessment.getAssessmentDate();

            if (!lastAssessmentsMap.containsKey(skillId)) {
                lastAssessmentsMap.put(skillId, assessment);
            } else {
                Assessment lastAssessment = lastAssessmentsMap.get(skillId);
                Date lastAssessmentDate = lastAssessment.getAssessmentDate();
                if (assessmentDate.after(lastAssessmentDate) || (assessmentDate.equals(lastAssessmentDate) && assessment.getIdAssessment() > lastAssessment.getIdAssessment())) {
                    lastAssessmentsMap.put(skillId, assessment);
                }
            }
        }

        return lastAssessmentsMap.values().stream()
                .map(this::mapAssessmentToDTO)
                .collect(Collectors.toList());
    }


//
//    public Map<Long, AssessmentDTO> getLastAssessmentsByCategoryAndEmployee(Long categoryId, Long employeeId) {
//        Map<Long, AssessmentDTO> lastAssessmentsBySkill = new HashMap<>();
//
//        CategoryDTO category = categoryService.getCategoryByIdWithSkills(categoryId);
//        List<SkillDTO> skills = category.getSkills();
//
//        for (SkillDTO skill : skills) {
//            List<AssessmentDTO> assessments = assessmentService.getLastAssessmentsByEmployee(employeeId);
//            AssessmentDTO lastAssessment = null;
//
//            for (AssessmentDTO assessment : assessments) {
//                if (assessment.getIdSkill().equals(skill.getIdSkill())) {
//                    if (lastAssessment == null || assessment.getAssessmentDate().after(lastAssessment.getAssessmentDate())) {
//                        lastAssessment = assessment;
//                    }
//                }
//            }
//
//            if (lastAssessment != null) {
//                lastAssessmentsBySkill.put(skill.getIdSkill(), lastAssessment);
//            }
//        }
//
//        return lastAssessmentsBySkill;
//    }

    //Calculate average rating for each category for specified employee
    @Override
    public Map<Long, Double> calculateAverageRatingsByCategoryAndEmployee(Long idEmployee) {
        Map<Long, Double> averageRatingsByCategory = new HashMap<>();

        List<CategoryDTO> categories = categoryService.getAll();

        for (CategoryDTO category : categories) {
            List<SkillDTO> skills = skillService.findSkillByCategory(category.getIdCategory());

            double totalRating = 0;
            int count = 0;

            for (SkillDTO skill : skills) {
                List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployeeAndSkillIdSkill(idEmployee, skill.getIdSkill());

                Assessment lastAssessment = null;

                for (Assessment assessment : assessments) {
                    if (lastAssessment == null || assessment.getAssessmentDate().after(lastAssessment.getAssessmentDate())
                            || (assessment.getAssessmentDate().equals(lastAssessment.getAssessmentDate()) && assessment.getIdAssessment() > lastAssessment.getIdAssessment())) {
                        lastAssessment = assessment;
                    }
                }

                if (lastAssessment != null && lastAssessment.getRating() != null) {
                    totalRating += lastAssessment.getRating();
                    count++;
                }
            }

            if (count > 0) {
                double averageRating = totalRating / count;
                double convertedRating = Math.round((averageRating / 3) * 100);

                averageRatingsByCategory.put(category.getIdCategory(), convertedRating);
            }
        }

        return averageRatingsByCategory;
    }

    //Assistance in specific area (the category as the problem area) for specified employee
    @Override
    public Map<Long, Double> getAssistance(Long idSkill, Long idEmployee) {
        Employee employee = employeeRepository.findById(idEmployee).orElse(null);
        if (employee != null) {
            List<EmployeeDTO> assists = employeeService.findAllEmployees().stream()
                    .filter(e -> e.getIdEmployee() != idEmployee)
                    .collect(Collectors.toList());
            Map<Long, Double> employeeRatings = new HashMap<>();
            for (EmployeeDTO assist : assists) {
                List<AssessmentDTO> assessments = getLastAssessmentsByEmployee(assist.getIdEmployee());
                AssessmentDTO lastAssessment = null;

                for (AssessmentDTO assessment : assessments) {
                    if (assessment.getIdSkill().equals(idSkill)) {
                        if (lastAssessment == null || assessment.getAssessmentDate().after(lastAssessment.getAssessmentDate())) {
                            lastAssessment = assessment;
                        }
                    }
                }

                if (lastAssessment != null) {
                    double rating = lastAssessment.getRating().doubleValue(); // Convert Integer to Double
                    employeeRatings.put(assist.getIdEmployee(), rating);
                }
            }

            // Convert ratings to percentages
            Map<Long, Double> assistance = new HashMap<>();
            for (Map.Entry<Long, Double> entry : employeeRatings.entrySet()) {
                double rating = entry.getValue();
                double percentage = (rating / 3) * 100; // Convert to percentage
                double roundedPercentage = Math.round(percentage * 100.0) / 100.0; // Round to two decimal places
                assistance.put(entry.getKey(), roundedPercentage);
            }

            // Sort the assistance map in descending order based on the ratings
            Map<Long, Double> sortedAssistance = assistance.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            return sortedAssistance;
        } else {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a manager.");
        }
    }






    //Calculate average rating for each category for specified experience level
    @Override
    public Map<Long, Double> calculateAverageRatingsByCategoryAndExperience(Experience experienceLevel) {
        Map<Long, Double> averageRatingsByCategory = new HashMap<>();

        List<CategoryDTO> categories = categoryService.getAll();

        for (CategoryDTO category : categories) {
            List<SkillDTO> skills = skillService.findSkillByCategory(category.getIdCategory());

            double totalRating = 0;
            int count = 0;

            List<EmployeeDTO> employees = employeeService.findEmployeesByExperience(experienceLevel);

            for (EmployeeDTO employee : employees) {
                for (SkillDTO skill : skills) {
                    List<Assessment> assessments = assessmentRepository.findByEmployeeIdEmployeeAndSkillIdSkill(employee.getIdEmployee(), skill.getIdSkill());

                    Assessment lastAssessment = null;

                    for (Assessment assessment : assessments) {
                        if (lastAssessment == null || assessment.getAssessmentDate().after(lastAssessment.getAssessmentDate())
                                || (assessment.getAssessmentDate().equals(lastAssessment.getAssessmentDate()) && assessment.getIdAssessment() > lastAssessment.getIdAssessment())) {
                            lastAssessment = assessment;
                        }
                    }

                    if (lastAssessment != null && lastAssessment.getRating() != null) {
                        totalRating += lastAssessment.getRating();
                        count++;
                    }
                }
            }

            if (count > 0) {
                double averageRating = totalRating / count;
                double convertedRating = Math.round((averageRating / 3) * 100);

                averageRatingsByCategory.put(category.getIdCategory(), convertedRating);
            }
        }

        return averageRatingsByCategory;
    }










    private Assessment mapDTOToAssessment(AssessmentDTO assessmentDTO) {
        Assessment assessment = new Assessment();
        assessment.setEmployee(employeeRepository.findById(assessmentDTO.getIdEmployee()).orElseThrow(() -> new RuntimeException("Employee not found")));
        assessment.setSkill(skillRepository.findById(assessmentDTO.getIdSkill()).orElseThrow(() -> new RuntimeException("Skill not found")));
        assessment.setRating(assessmentDTO.getRating());
       // assessment.setComment(assessmentDTO.getComment());
        assessment.setAssessmentDate(assessmentDTO.getAssessmentDate());
        return assessment;
    }
        @Override
        public AssessmentDTO mapAssessmentToDTO(Assessment assessment) {
            AssessmentDTO assessmentDTO = new AssessmentDTO();
            assessmentDTO.setIdAssessment(assessment.getIdAssessment());
            assessmentDTO.setIdEmployee(assessment.getEmployee().getIdEmployee());
            assessmentDTO.setIdSkill(assessment.getSkill().getIdSkill());
            assessmentDTO.setRating(assessment.getRating());
         //   assessmentDTO.setComment(assessment.getComment());
            assessmentDTO.setAssessmentDate(assessment.getAssessmentDate());
            return assessmentDTO;
        }
}