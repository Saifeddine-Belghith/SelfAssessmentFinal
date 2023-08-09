package com.altersis.skillmatrix.personaltarget;

import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalTargetRepository extends JpaRepository<PersonalTarget, Long> {
    List<PersonalTarget> findAllByEmployeeIdEmployee(Long idEmployee);
    List<PersonalTarget> findByTargetAreaIn(List<TargetArea> targetAreas);
    List<PersonalTarget> findByStatusIn(List<TargetStatus> targetStatuses);
    List<PersonalTarget> findBySupportedValueIn(List<SupportedValue> supportedValues);
    List<PersonalTarget> findByTargetAreaInOrStatusInOrSupportedValueIn(List<TargetArea> targetAreas, List<TargetStatus> targetStatuses, List<SupportedValue> supportedValues);
    List<PersonalTarget> findBySkillAndStatusInAndTargetDateAndQuarterIn(String skill, List<TargetStatus> targetStatuses,  int targetDate,List<String> quarters);
}
