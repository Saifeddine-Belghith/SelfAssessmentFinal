package com.altersis.skillmatrix.ClientFeedback;

import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface ClientFeedbackService {
    ClientFeedbackDTO createClientFeedback(ClientFeedbackDTO clientFeedbackDTO, Long idEmployee);

    List<ClientFeedback> getAllClientFeedbacks();

    Optional<ClientFeedback> getClientFeedbackById(Long idClientFeedback) throws ResourceNotFoundException;

    List<ClientFeedbackDTO> getClientFeedbackByEmployee(Long idEmployee) throws ResourceNotFoundException;
    void deleteClientFeedback(Long idClientFeedback) throws ResourceNotFoundException;

//    ClientFeedback updateTarget(Long idClientFeedback, ClientFeedback clientFeedback) throws ResourceNotFoundException;
}
