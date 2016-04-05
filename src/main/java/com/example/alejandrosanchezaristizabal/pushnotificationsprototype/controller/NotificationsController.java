package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.Notification;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.service.GcmNotificationsSender;

@RestController
public class NotificationsController {

  private final GcmNotificationsSender gcmNotificationsSender;

  @Autowired
  public NotificationsController(GcmNotificationsSender gcmNotificationsSender) 
    {
    this.gcmNotificationsSender = gcmNotificationsSender;
  }

  /**
   * Sends a notification to GCM so that it can be pushed to the user.
   */
  @RequestMapping(value = "/pushNotification", method = RequestMethod.POST, 
    headers = "Content-Type=application/json")
  public @ResponseBody HttpEntity<Notification> pushNotification
    (@RequestBody Notification notification) {
    System.out.println("Notification to push: " + notification.toString());
    gcmNotificationsSender.sendNotificationToGcm(notification);

    return new ResponseEntity<>(notification, HttpStatus.CREATED);
  }
}