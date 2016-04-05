package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model;

import java.util.Arrays;

public class Notification {

  private String title;
  private String body;
  private String[] userIds;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String[] getUserIds() {
    return userIds;
  }

  public void setUserIds(String[] userIds) {
    this.userIds = userIds;
  }
  
  @Override
  public String toString() {
    return "{title = '" + title + "', body = '" + body + "', userIds = " 
      + (userIds == null ? null : Arrays.asList(userIds)) + "}";
  }
}