package com.security.jwttoken.model;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

public class User implements UserDetails { // UserDetails, Security ait bir sınıf olduğunu belirtir

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String username;
  private String password;

  private boolean accountNonExpired;
  private boolean isEnabled;
  private boolean accountNonLocked;
  private boolean credentialNonExpired;
  private boolean isCredentialsNonExpired;

  // kullanıcının rollerinin tamamını farklı tabloda(liste) tuttuk
  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<Role> authorities;

}
