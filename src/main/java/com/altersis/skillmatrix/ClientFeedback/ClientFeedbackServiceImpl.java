package com.altersis.skillmatrix.ClientFeedback;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.enumeration.Evaluation;
import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.personaltarget.PersonalTarget;
import com.altersis.skillmatrix.personaltarget.PersonalTargetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientFeedbackServiceImpl implements ClientFeedbackService {

    private final ClientFeedbackRepository clientFeedbackRepository;
@Autowired
    private EmployeeRepository employeeRepository;

    public ClientFeedbackServiceImpl(ClientFeedbackRepository clientFeedbackRepository) {
        this.clientFeedbackRepository = clientFeedbackRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ClientFeedbackDTO createClientFeedback(ClientFeedbackDTO clientFeedbackDTO, Long idEmployee) {
        Employee employee = employeeRepository.getById(idEmployee);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + idEmployee + " not found.");
        }
        ClientFeedback clientFeedback = new ClientFeedback();
        clientFeedback.setClientName(clientFeedbackDTO.getClientName());

        clientFeedback.setTrend(clientFeedbackDTO.getTrend());
        clientFeedback.setEmployee(employee);
        // Convert the rating to a string
        Evaluation eval = Evaluation.valueOf(clientFeedbackDTO.getEvaluation());
        clientFeedback.setEvaluation(eval);
        // Determine the rating based on the evaluation
        double rating = 0;
        switch (eval) {
            case EXCELLENT:
                rating = 3.0;
                break;
            case GOOD:
                rating = 2.5;
                break;
            case APPROPRIATE:
                rating = 2.0;
                break;
            case UNDER_EXPECTATION:
                rating = 1.5;
                break;
            case POOR:
                rating = 1.0;
                break;
            case NOT_APPLICABLE:
                rating = 0.5;
                break;
            default:
                throw new IllegalArgumentException("Invalid evaluation: " + clientFeedbackDTO.getEvaluation());
        }
        clientFeedback.setRating(rating);

        clientFeedback.setComment(clientFeedbackDTO.getComment());

        ClientFeedback savedClientFeedback = clientFeedbackRepository.save(clientFeedback);

        return new ClientFeedbackDTO(savedClientFeedback);
    }

    @Override
    public List<ClientFeedback> getAllClientFeedbacks() {
        return clientFeedbackRepository.findAll();
    }

    @Override
    public Optional<ClientFeedback> getClientFeedbackById(Long idClientFeedback) throws ResourceNotFoundException {
        return clientFeedbackRepository.findById(idClientFeedback);
    }

    @Override
    public void deleteClientFeedback(Long idClientFeedback) throws ResourceNotFoundException {
        if (clientFeedbackRepository.existsById(idClientFeedback)) {
            clientFeedbackRepository.deleteById(idClientFeedback);
        } else {
            throw new ResourceNotFoundException("Target not found with id: " + idClientFeedback);
        }
    }

    @Override
    public List<ClientFeedbackDTO> getClientFeedbackByEmployee(Long idEmployee) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + idEmployee));

        List<ClientFeedback> clientFeedbacks = clientFeedbackRepository.findAllByEmployeeIdEmployee(idEmployee);
        List<ClientFeedbackDTO> clientFeedbackDTOS = new ArrayList<>();

        for (ClientFeedback clientFeedback : clientFeedbacks) {
            clientFeedbackDTOS.add(new ClientFeedbackDTO(clientFeedback));
        }

        return clientFeedbackDTOS;
    }
}
