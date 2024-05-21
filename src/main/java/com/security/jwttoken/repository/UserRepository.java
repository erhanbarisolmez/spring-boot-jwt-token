package com.security.jwttoken.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.jwttoken.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


  Optional<User>findById(Long id);
  Optional<User> findByUsername(String userName);
  List<User> findAll();
  @SuppressWarnings("unchecked")
  User save(User user);
  void delete(User user);
  void deleteById(Long id);
}