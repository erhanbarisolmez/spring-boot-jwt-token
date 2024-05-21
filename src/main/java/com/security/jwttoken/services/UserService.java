package com.security.jwttoken.services;

import static java.util.Objects.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.jwttoken.dto.CreateUserRequest;
import com.security.jwttoken.model.User;
import com.security.jwttoken.repository.UserRepository;
import com.security.jwttoken.utils.configPasswordEncoder.PasswordEncoderConfig;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  private final PasswordEncoderConfig passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoderConfig passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    return user.orElseThrow(EntityNotFoundException::new);
  }



  public User createUser(CreateUserRequest req) {
    User user = User.builder()
        .name(requireNonNull(req.name(), "İsim boş olamaz"))
        .username(requireNonNull(req.username()))
        .password(passwordEncoder.bCryptPasswordEncoder().encode(req.password()))
        .authorities(req.authorities())
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialNonExpired(true)
        .isCredentialsNonExpired(true)
        .isEnabled(true)
        .build();
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
