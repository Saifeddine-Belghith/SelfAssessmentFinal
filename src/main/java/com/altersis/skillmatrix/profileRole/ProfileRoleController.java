package com.altersis.skillmatrix.profileRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profilerole")
public class ProfileRoleController {
    private final ProfileRoleService profileRoleService;
    @Autowired
    public ProfileRoleController(ProfileRoleService profileRoleService) {
        this.profileRoleService = profileRoleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProfileRole>> getAllProfileRoles() {
        List<ProfileRole> profileRoles = profileRoleService.getAllProfileRoles();
        return ResponseEntity.ok(profileRoles);
    }

    @GetMapping("/{idProfileRole}")
    public ResponseEntity<ProfileRole> getProfileRoleById(@PathVariable Long idProfileRole) {
        Optional<ProfileRole> profileRole = profileRoleService.getProfileRoleById(idProfileRole);
        return profileRole.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileRole> createProfileRole(@RequestBody ProfileRole profileRole) {
        ProfileRole savedProfileRole = profileRoleService.saveProfileRole(profileRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfileRole);
    }

    @DeleteMapping("/{idProfileRole}")
    public ResponseEntity<Void> deleteProfileRole(@PathVariable Long idProfileRole) {
        profileRoleService.deleteProfileRole(idProfileRole);
        return ResponseEntity.noContent().build();
    }

    // This endpoint returns a map of category id to minimum scores for a target role
    @GetMapping("/{idProfileRole}/minscores")
    public ResponseEntity<Map<String, Double>> getCategoryMinScoresMapById(@PathVariable Long idProfileRole) {
        Map<String, Double> categoryMinScoresMap = profileRoleService.getCategoryMinScoresMapById(idProfileRole);
        return ResponseEntity.ok(categoryMinScoresMap);
    }

}
