package com.altersis.skillmatrix.manager;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.team.Team;
import com.altersis.skillmatrix.team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final TeamRepository teamRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository, TeamRepository teamRepository) {
        this.managerRepository = managerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<ManagerDTO> findAll() {
        List<Manager> managers = managerRepository.findAll();
        List<ManagerDTO> managerDTOs = new ArrayList<>();
        for (Manager manager : managers) {
            ManagerDTO managerDTO = convertToDTO(manager);
            managerDTOs.add(managerDTO);
        }
        return managerDTOs;
    }

    public void assignManager(Long employeeId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + managerId));

        employee.addManager(manager);

        employeeRepository.save(employee);
        employeeRepository.save(manager);
    }

//    @Override
//    public List<Employee> getAllEmployeesByManagerId(Long managerId) {
//        return employeeRepository.findByManagerId(managerId);
//    }




    @Override
    public ManagerDTO findById(Long id) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (optionalManager.isPresent()) {
            Manager manager = optionalManager.get();
            return convertToDTO(manager);
        }
        return null;
    }

    @Override
    public ManagerDTO save(ManagerDTO managerDTO) {
        Manager manager = convertToEntity(managerDTO);
        manager = managerRepository.save(manager);
        return convertToDTO(manager);
    }

    @Override
    public boolean deleteById(Long id) {
        if (managerRepository.existsById(id)) {
            managerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ManagerDTO update(Long id, ManagerDTO managerDTO) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if (optionalManager.isPresent()) {
            Manager manager = optionalManager.get();
            manager.setFirstName(managerDTO.getFirstName());
            manager.setLastName(managerDTO.getLastName());
            manager.setEmail(managerDTO.getEmail());
            manager.setPassword(managerDTO.getPassword());
            manager.setRole(managerDTO.getRole());

            if (managerDTO.getTeam() != null) {
                Optional<Team> optionalTeam = teamRepository.findById(managerDTO.getTeam().getId());
                if (optionalTeam.isPresent()) {
                    Team team = optionalTeam.get();
                    manager.setTeam(team);
                }
            } else {
                manager.setTeam(null);
            }

            manager = managerRepository.save(manager);
            return convertToDTO(manager);
        }
        return null;
    }

    private ManagerDTO convertToDTO(Manager manager) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setIdEmployee(manager.getIdEmployee());
        managerDTO.setFirstName(manager.getFirstName());
        managerDTO.setLastName(manager.getLastName());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setPassword(manager.getPassword());
        managerDTO.setRole(manager.getRole());
        managerDTO.setTeam(manager.getTeam());
        return managerDTO;
    }

    private Manager convertToEntity(ManagerDTO managerDTO) {
        Manager manager = new Manager();
        manager.setIdEmployee(managerDTO.getIdEmployee());
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setEmail(managerDTO.getEmail());
        manager.setPassword(managerDTO.getPassword());
        manager.setRole(managerDTO.getRole());

        if (managerDTO.getTeam() != null) {
            Optional<Team> optionalTeam = teamRepository.findById(managerDTO.getTeam().getId());
            if (optionalTeam.isPresent()) {
                Team team = optionalTeam.get();
                manager.setTeam(team);
            }
        }

        return manager;
    }
}
