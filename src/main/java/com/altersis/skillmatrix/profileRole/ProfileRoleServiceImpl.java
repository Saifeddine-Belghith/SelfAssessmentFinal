package com.altersis.skillmatrix.profileRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileRoleServiceImpl implements ProfileRoleService {

    private final ProfileRoleRepository profileRoleRepository;
    @Autowired
    public ProfileRoleServiceImpl(ProfileRoleRepository profileRoleRepository) {
        this.profileRoleRepository = profileRoleRepository;
    }

    @Override
    public List<ProfileRole> getAllProfileRoles() {
        return profileRoleRepository.findAll();
    }
    @Override
    public Optional<ProfileRole> getProfileRoleById(Long idProfileRole) {
        return profileRoleRepository.findById(idProfileRole);
    }

    @Override
    public ProfileRole saveProfileRole(ProfileRole profileRole) {
        return profileRoleRepository.save(profileRole);
    }

    @Override
    public void deleteProfileRole(Long idProfileRole) {
        profileRoleRepository.deleteById(idProfileRole);
    }
    @Override
    public Map<String, Double> getCategoryMinScoresMapById(Long idProfileRole) {
        Optional<ProfileRole> profileRoleOptional = profileRoleRepository.findById(idProfileRole);
        if (profileRoleOptional.isPresent()) {
            ProfileRole profileRole = profileRoleOptional.get();
            return profileRole.getCategoryMinScoresMap();
        }
        return Collections.emptyMap(); // Return an empty map if the target role doesn't exist
    }



}