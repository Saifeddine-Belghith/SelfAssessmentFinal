package com.altersis.skillmatrix.team;

import java.util.List;

public interface TeamService {
    List<TeamDTO> findAll();
    TeamDTO findById(Long id);
    TeamDTO save(TeamDTO teamDTO);
    boolean deleteById(Long id);
    TeamDTO update(Long id, TeamDTO teamDTO);
}
