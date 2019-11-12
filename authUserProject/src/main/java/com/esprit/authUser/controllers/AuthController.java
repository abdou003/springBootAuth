package com.esprit.authUser.controllers;


import static org.springframework.http.ResponseEntity.ok;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.management.loading.MLet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.authUser.configs.JwtTokenFilter;
import com.esprit.authUser.configs.JwtTokenProvider;
import com.esprit.authUser.models.User;
import com.esprit.authUser.repositories.UserRepository;
import com.esprit.authUser.services.CustomUserDetailService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Autowired
 AuthenticationManager authenticationManager;

 @Autowired
 JwtTokenProvider jwtTokenProvider;

 @Autowired
 UserRepository users;

 @Autowired
 private CustomUserDetailService userService;

 @SuppressWarnings("rawtypes")
 @PostMapping("/login")
 public ResponseEntity login(@RequestBody AuthBody data) {
  try {
   String username = data.getEmail();
   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
   String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRole());
   Map<Object, Object> model = new HashMap<>();
   model.put("username", username);
   model.put("token", token);
   return ok(model);
  } catch (AuthenticationException e) {
   throw new BadCredentialsException("Invalid email/password supplied");
  }
 }

 @SuppressWarnings("rawtypes")
 @PostMapping("/register")
 public ResponseEntity register(@RequestBody User user) {
  User userExists = userService.findUserByEmail(user.getEmail());
  if (userExists != null) {
   throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
  }
  userService.saveUser(user);
  Map<Object, Object> model = new HashMap<>();
  model.put("message", "User registered successfully");
  return ok(model); 
 }
 
 @RequestMapping(value = "/username", method = RequestMethod.GET)
 @ResponseBody
 public ResponseEntity currentUserName(Authentication authentication) {
	 Map<Object, Object> model = new HashMap<>();
	 UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	  model.put("obj",userDetails);
     return ok(model);
 }
 
 @SuppressWarnings("rawtypes")
 @GetMapping("/delete/{id}")
 public ResponseEntity deleteProfile(@PathVariable("id") Long id,Authentication authentication) {
	 UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	 User us;
	 us =userService.findUserByEmailAndPassword(userDetails.getUsername(),userDetails.getPassword());
	 Map<Object, Object> model = new HashMap<>();

	 if(us.getId()==id) {
		 userService.deleteUser(id);
		 model.put("message", "User deleted successfully");
		  return ok(model); 	 
	 }
	 model.put("message", "not allowed");
	 return ok(model);
	 
 }
 @SuppressWarnings("rawtypes")
 @PostMapping("/update/{id}")
 public ResponseEntity updateProfile(@PathVariable("id") Long id,@RequestBody User user,Authentication authentication,HttpServletRequest request,HttpServletResponse response) {
	 UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	 User us;
	 us =userService.findUserByEmailAndPassword(userDetails.getUsername(),userDetails.getPassword());
	 Map<Object, Object> model = new HashMap<>();
	 if(us.getId()==id) {
		 userService.updateUser(id, user);
		 model.put("message", "User updated successfully");
		 return ok(model); 
	 }
	 
	 model.put("message", "not allowed");
	 return ok(model);
 }
 

}