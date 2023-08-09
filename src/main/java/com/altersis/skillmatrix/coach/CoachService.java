package com.altersis.skillmatrix.coach;
import com.altersis.skillmatrix.employee.EmployeeDTO;

import java.util.List;
public interface CoachService {
    void assignCoach(Long coacheeId, Long coachId);

    CoachDTO save(CoachDTO coachDTO);
   // List<CoachDTO> findAll();
    CoachDTO findById(Long id);
    void deleteById(Long id);
  List<EmployeeDTO> getCoacheesByCoachId(Long coachId);
    void assignCoacheeToCoach(Long coacheeId, Long coachId);
 public CoachDTO getCoachByCoachId(Long coachId);

}
