package com.esprit.authUser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.esprit.authUser.models.Role;
import com.esprit.authUser.repositories.RoleRepository;

@SpringBootApplication
public class AuthUserProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthUserProjectApplication.class, args);
	}
	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {

	  return args -> {

	    Role adminRole = roleRepository.findByRole("ADMIN");
	    if (adminRole == null) {
	      Role newAdminRole = new Role();
	      newAdminRole.setRole("ADMIN");
	      roleRepository.save(newAdminRole);
	    }
	  };

	}

}
