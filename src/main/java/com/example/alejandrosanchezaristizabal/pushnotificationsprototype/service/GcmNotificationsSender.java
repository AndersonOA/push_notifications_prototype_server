package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.service;

import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.Notification;

public interface GcmNotificationsSender {

  void sendNotificationToGcm(Notification notification);
}