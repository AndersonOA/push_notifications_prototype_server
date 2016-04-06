package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

  @Id @GeneratedValue
  private Long id;
  private String name;
  private String registrationId;
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(String registrationId) {
    this.registrationId = registrationId;
  }
  
  @Override
  public String toString() {
    return "{id = '" + id + "', name = '" + name + "', registrationId = '" 
      + registrationId  + "'}";
  }
}