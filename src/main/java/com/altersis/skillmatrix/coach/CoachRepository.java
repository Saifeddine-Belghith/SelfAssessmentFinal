package com.altersis.skillmatrix.coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepository extends JpaRepository <Coach, Long>{
    Coach findByIdEmployee(Long id);
}
