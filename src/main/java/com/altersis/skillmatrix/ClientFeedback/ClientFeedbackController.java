package com.altersis.skillmatrix.ClientFeedback;

import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.personaltarget.PersonalTargetDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientfeedbacks")
public class ClientFeedbackController {
    private final ClientFeedbackService clientFeedbackService;

    public ClientFeedbackController(ClientFeedbackService clientFeedbackService) {
        this.clientFeedbackService = clientFeedbackService;
    }

    //Create ClientFeedback ( for specific Employee) (only manager)
    @PostMapping("/create/{idEmployee}")
    public ResponseEntity<ClientFeedbackDTO> createClientFeedback(@PathVariable Long idEmployee,
            @RequestBody ClientFeedbackDTO clientFeedbackDTO) {
        ClientFeedbackDTO createdClientFeedback = clientFeedbackService.createClientFeedback(clientFeedbackDTO, idEmployee);
        return ResponseEntity.ok(createdClientFeedback);
    }

    //Get ClientFeedback by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientFeedback>> getClientFeedback(@PathVariable Long idClientFeedback) throws ResourceNotFoundException {
        Optional<ClientFeedback> clientFeedback = clientFeedbackService.getClientFeedbackById(idClientFeedback);
        return ResponseEntity.ok(clientFeedback);
    }

    //Get all clent feddback
    @GetMapping("/all")
    public ResponseEntity<List<ClientFeedback>> getAllClientFeedbacks() {
        List<ClientFeedback> clientFeedbacks = clientFeedbackService.getAllClientFeedbacks();
        return ResponseEntity.ok(clientFeedbacks);
    }

    //get all client feedback by Employee
    @GetMapping("/employee/{idEmployee}")
    public ResponseEntity<List<ClientFeedbackDTO>> getClientFeedbackByEmployee(@PathVariable Long idEmployee) {
        try {
            List<ClientFeedbackDTO> clientFeedbacks = clientFeedbackService.getClientFeedbackByEmployee(idEmployee);
            return ResponseEntity.ok(clientFeedbacks);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Update ClientFeedback
//    @PutMapping("update/{idTarget}")
//    public ResponseEntity<Target> updateClientFeedback(@PathVariable Long idTarget, @RequestBody Target target) throws ResourceNotFoundException {
////        ClientFeedback.setIdTarget(idTarget);
//        Target updatedTarget = clientFeedbackService.updateTarget(idTarget, target);
//        return ResponseEntity.ok(updatedTarget);
//    }

    //Delete ClientFeedback
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteClientFeedback(@PathVariable Long idClientFeedback) {
        clientFeedbackService.deleteClientFeedback(idClientFeedback);
        return ResponseEntity.noContent().build();
    }
}
