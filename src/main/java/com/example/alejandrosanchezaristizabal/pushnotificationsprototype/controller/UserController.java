package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.User;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.repository.UserRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class UserController {

  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Saves the user with his respective Registration-ID for GCM.
   */
  @RequestMapping(value = "/register", method = RequestMethod.POST, 
    headers = "Content-Type=application/json")
  public @ResponseBody User saveRegistrationId(@RequestBody User userJson) {
    System.out.println("User to save: " + userJson.toString());
    User user = userRepository
      .findByRegistrationId(userJson.getRegistrationId());
    if (user == null) {
      // The Registration-ID didn't exist yet.
      user = userRepository.save(userJson);
      System.out.println("Registration-ID didn't exist and thus was saved");
    }
    if (user == null) {
      // The user wasn't properly saved.
      user = new User();
      System.out.println("Registration-ID couldn't be saved");
    }

    return user;
  }
  
  /**
   * Updates the user with his refreshed Registration-ID for GCM.
   */
  @RequestMapping(value = "/updateRegistrationId", method = RequestMethod.POST, 
    headers = "Content-Type=application/json")
  public @ResponseBody User updateRegistrationId(@RequestBody User userJson) {
    System.out.println("User to update: " + userJson.toString());
    User user = userRepository.findOne(userJson.getId());
    if (user != null) {
      // The Registration-ID already exist.
      user.setRegistrationId(userJson.getRegistrationId());
      user = userRepository.save(user);
      System.out.println("Registration-ID was successfully updated");
    }
    if (user == null) {
      // The user didn't exist or wasn't properly saved.
      user = new User();
      System.out.println("Registration-ID couldn't be updated");
    }

    return user;
  }
}