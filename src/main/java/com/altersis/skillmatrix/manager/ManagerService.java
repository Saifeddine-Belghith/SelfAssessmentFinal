package com.altersis.skillmatrix.manager;

import java.util.List;

public interface ManagerService {
    void assignManager(Long employeeId, Long managerId);
    List<ManagerDTO> findAll();

//    List<Employee> getAllEmployeesByManagerId(Long managerId);

    ManagerDTO findById(Long id);
    ManagerDTO save(ManagerDTO managerDTO);
    boolean deleteById(Long id);
    ManagerDTO update(Long id, ManagerDTO managerDTO);
}
