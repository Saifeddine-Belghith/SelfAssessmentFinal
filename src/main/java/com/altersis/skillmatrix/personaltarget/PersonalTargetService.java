package com.altersis.skillmatrix.personaltarget;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;

import java.util.List;

public interface PersonalTargetService {
    PersonalTargetDTO createPersonalTarget(PersonalTargetDTO personalTargetDTO, Long idEmployee);
    PersonalTargetDTO assignPersonalTarget(PersonalTargetDTO personalTargetDTO, Long idEmployee, Long originId);
    PersonalTargetDTO updatePersonalTargetStatus(Long personalTargetId, TargetStatus status) throws ResourceNotFoundException;
    void deletePersonalTarget(Long idPersonalTarget);
    List<PersonalTargetDTO> getPersonalTargetsByEmployee(Long idEmployee) throws ResourceNotFoundException;
    List<EmployeeDTO> searchConsultantsByTarget(String skill, /*List<TargetArea> targetAreas,*/ List<TargetStatus> targetStatuses, /*List<SupportedValue> supportedValues,*/ int targetDate, List<String> quarters);
//    Optional<PersonalTarget> findById(Long id);
//    PersonalTargetDTO createPersonalTarget(PersonalTargetDTO personalTargetDTO) throws Exception;
//
//    List<PersonalTargetDTO> getPersonalTargetsByCoach(Long idCoach);
//    PersonalTargetDTO createPersonalTargetForEmployee(PersonalTargetDTO personalTargetDTO, Long idEmployee) throws Exception;



}
