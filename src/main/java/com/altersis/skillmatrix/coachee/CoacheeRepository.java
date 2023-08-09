package com.altersis.skillmatrix.coachee;

import com.altersis.skillmatrix.coach.CoachDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoacheeRepository extends JpaRepository<Coachee, Long> {
    List<Coachee> findByCoach_IdCoach(Long coachId);
    Optional<CoachDTO> findByIdEmployee(Long id);
}
