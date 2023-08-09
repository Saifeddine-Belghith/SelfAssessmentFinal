package com.altersis.skillmatrix.profileRole;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfileRoleService {
    List<ProfileRole> getAllProfileRoles();
    Optional<ProfileRole> getProfileRoleById(Long idProfileRole);
    ProfileRole saveProfileRole(ProfileRole profileRole);
    void deleteProfileRole(Long idProfileRole);
    Map<String, Double> getCategoryMinScoresMapById(Long idProfileRole);
}
