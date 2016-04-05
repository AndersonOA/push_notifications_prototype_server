package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByRegistrationId(String registrationId);
}
