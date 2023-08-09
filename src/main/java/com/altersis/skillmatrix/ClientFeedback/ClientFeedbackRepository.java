package com.altersis.skillmatrix.ClientFeedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientFeedbackRepository extends JpaRepository<ClientFeedback, Long> {
    List<ClientFeedback> findAllByEmployeeIdEmployee(Long idEmployee);
}
