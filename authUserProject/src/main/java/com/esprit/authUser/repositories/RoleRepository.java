package com.esprit.authUser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprit.authUser.models.Role;



public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRole(String role);
}
