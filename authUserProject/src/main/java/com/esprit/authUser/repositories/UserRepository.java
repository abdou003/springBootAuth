package com.esprit.authUser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.esprit.authUser.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String username);
	User findByEmailAndPassword(String email ,String password);
	
	@Modifying
	@Query("delete from User u where u.id=:id")
	void deleteUser(@Param("id") Long id);
	@Modifying
	@Query("UPDATE User u SET u.email= :email ,u.username= :username ,u.password= :password WHERE u.id = :id")
	void updateUser(@Param("id") Long id, @Param("email") String email,@Param("username") String username,@Param("password") String password);
}
