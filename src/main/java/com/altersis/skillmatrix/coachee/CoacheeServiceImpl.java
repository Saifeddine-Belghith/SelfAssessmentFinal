package com.altersis.skillmatrix.coachee;

import com.altersis.skillmatrix.coach.CoachRepository;
import com.altersis.skillmatrix.coach.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoacheeServiceImpl implements CoacheeService {

@Autowired
private CoachService coachService;
@Autowired
private CoachRepository coachRepository;
@Autowired
private CoacheeRepository coacheeRepository;
//    public Coachee createNewCoachee(String firstName, String lastName, String email, String password, String phone, Experience experienceLevel, Long coachId) {
//        CoachDTO CoachDTO = coachRepository.findByIdCoach(coachId);
//        CoachDTO coachDTO = CoachDTO;
//
//// create new coachee
//        CoacheeDTO coacheeDTO = new CoacheeDTO();
//        coacheeDTO.setFirstName(firstName);
//        coacheeDTO.setLastName(lastName);
//        coacheeDTO.setEmail(email);
//        coacheeDTO.setPassword(password);
//        coacheeDTO.setPhone(phone);
//        coacheeDTO.setExperienceLevel(experienceLevel);
//        coacheeDTO.setRole("ROLE_COACHEE");
//        coacheeDTO.setCoach(coachDTO);
//
//        // save coachee
//        coacheeRepository.save(coacheeDTO);
//
   }
