package com.esprit.authUser.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esprit.authUser.models.Role;
import com.esprit.authUser.models.User;
import com.esprit.authUser.repositories.RoleRepository;

import com.esprit.authUser.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		 return userRepository.findByEmail(email);
		}
	public User findUserByEmailAndPassword(String email,String password) {
		 return userRepository.findByEmailAndPassword(email, password);
		}
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		Role userRole = roleRepository.findByRole("ADMIN");
		user.setRole(userRole);
		userRepository.save(user);
		}
	@Transactional
	public void deleteUser(Long id) {
		userRepository.deleteUser(id);
		}
	@Transactional
	public void updateUser(Long id,User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.updateUser(id, user.getEmail(), user.getUsername(), user.getPassword());
		}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

	User user = userRepository.findByEmail(email);
	if(user != null) {
	List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
	return buildUserForAuthentication(user, authorities);
	} else {
	throw new UsernameNotFoundException("username not found");
	}
	}
	
	
	private List<GrantedAuthority> getUserAuthority(Role userRole) {
		Set<GrantedAuthority> roles = new HashSet<>();
		
		roles.add(new SimpleGrantedAuthority(userRole.getRole()));


		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
		}
	
	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		 return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
		}

}
