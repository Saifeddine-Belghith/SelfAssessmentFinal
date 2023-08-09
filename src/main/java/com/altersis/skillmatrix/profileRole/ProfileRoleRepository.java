package com.altersis.skillmatrix.profileRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRoleRepository extends JpaRepository<ProfileRole, Long> {

}
