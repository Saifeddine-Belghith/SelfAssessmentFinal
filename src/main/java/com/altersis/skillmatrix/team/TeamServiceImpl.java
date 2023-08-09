package com.altersis.skillmatrix.team;


import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.manager.Manager;
import com.altersis.skillmatrix.manager.ManagerDTO;
import com.altersis.skillmatrix.manager.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;

    public TeamServiceImpl(TeamRepository teamRepository, ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<TeamDTO> findAll() {
        List<Team> teams = teamRepository.findAll();
        List<TeamDTO> teamDTOS = new ArrayList<>();
        for (Team team : teams) {
            teamDTOS.add(convertToDto(team));
        }
        return teamDTOS;
    }

    @Override
    public TeamDTO findById(Long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            return convertToDto(teamOptional.get());
        }
        return null;
    }

    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        Team team = convertToEntity(teamDTO);

        // set manager
        ManagerDTO managerDTO = teamDTO.getManager();
        if (managerDTO != null) {
            Optional<Manager> managerOptional = managerRepository.findById(managerDTO.getIdEmployee());
            if (managerOptional.isPresent()) {
                team.setManager(managerOptional.get());
            }
        }

        // set employees
        List<EmployeeDTO> employeeDTOList = teamDTO.getEmployees();
        if (employeeDTOList != null && !employeeDTOList.isEmpty()) {
            List<Employee> employees = new ArrayList<>();
            for (EmployeeDTO employeeDTO : employeeDTOList) {
                Optional<Employee> employeeOptional = employeeRepository.findById(employeeDTO.getIdEmployee());
                if (employeeOptional.isPresent()) {
                    employees.add(employeeOptional.get());
                }
            }
            team.setEmployees(employees);
        } else {
            team.setEmployees(null);
        }


        team = teamRepository.save(team);
        return convertToDto(team);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public TeamDTO update(Long id, TeamDTO teamDTO) {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.setName(teamDTO.getName());

            // set manager
            ManagerDTO managerDTO = teamDTO.getManager();
            if (managerDTO != null) {
                Optional<Manager> managerOptional = managerRepository.findById(managerDTO.getIdEmployee());
                if (managerOptional.isPresent()) {
                    team.setManager(managerOptional.get());
                }
            } else {
                team.setManager(null);
            }

            // set employees
            List<EmployeeDTO> employeeDTOList = teamDTO.getEmployees();
            if (employeeDTOList != null && !employeeDTOList.isEmpty()) {
                List<Employee> employees = new ArrayList<>();
                for (EmployeeDTO employeeDTO : employeeDTOList) {
                    Optional<Employee> employeeOptional = employeeRepository.findById(employeeDTO.getIdEmployee());
                    if (employeeOptional.isPresent()) {
                        employees.add(employeeOptional.get());
                    }
                }
                team.setEmployees(employees);
            } else {
                team.setEmployees(null);
            }

            team = teamRepository.save(team);
            return convertToDto(team);
        }
        return null;
    }

    private TeamDTO convertToDto(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());

        // set manager
        Manager manager = (Manager) team.getManager();
        if (manager != null) {
            ManagerDTO managerDTO = new ManagerDTO();
            managerDTO.setIdEmployee(manager.getIdEmployee());
            managerDTO.setFirstName(manager.getFirstName());
            managerDTO.setLastName(manager.getLastName());
            managerDTO.setEmail(manager.getEmail());
            managerDTO.setPassword(manager.getPassword());
            managerDTO.setRole(manager.getRole());
            teamDTO.setManager(managerDTO);
        }

        // set employees
        List<Employee> employees = team.getEmployees();
        if (employees != null && !employees.isEmpty()) {
            List<EmployeeDTO> employeeDTOList = new ArrayList<>();
            for (Employee employee : employees) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setIdEmployee(employee.getIdEmployee());
                employeeDTO.setFirstName(employee.getFirstName());
                employeeDTO.setLastName(employee.getLastName());
                employeeDTO.setEmail(employee.getEmail());
                employeeDTO.setPassword(employee.getPassword());
                employeeDTO.setRole(employee.getRole());
                employeeDTOList.add(employeeDTO);
            }
            teamDTO.setEmployees(employeeDTOList);
        }

        return teamDTO;
    }

    private Team convertToEntity(TeamDTO teamDTO) {
        Team team = new Team();
        team.setName(teamDTO.getName());
        return team;
    }
}
