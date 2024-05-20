package com.security.jwttoken.services;

import static java.util.Objects.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.User;
import com.security.jwttoken.repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    return user.orElseThrow(EntityNotFoundException::new);
  }

  public Optional<User> getByUserName(String username) {
    return userRepository.findByUsername(username);
  }

  public User createUser(@Valid @RequestBody CreateUserRequest req) {
      
    User user = User.builder()
        .name(requireNonNull(req.name(), "İsim boş olamaz"))
        .username(requireNonNull(req.username()))
        .password(passwordEncoder.encode(req.password()))
        .authorities(req.authorities())
        .build();
        
    log.info("*********************************************"+user.toString());

    return userRepository.save(user);

  }


  public void deleteById(Long id){
    userRepository.deleteById(id);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void createUserIfNotExists(CreateUserRequest request) {
    Optional<User> existingUser = userRepository.findByUsername(request.username());
    if (existingUser.isEmpty()) {
      createUser(request);
    }
  }

}
