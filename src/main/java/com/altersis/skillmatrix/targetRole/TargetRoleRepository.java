package com.altersis.skillmatrix.targetRole;

import com.altersis.skillmatrix.personaltarget.PersonalTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetRoleRepository extends JpaRepository<TargetRole, Long>{
    List<TargetRole> findAllByEmployeeIdEmployee(Long idEmployee);
}
