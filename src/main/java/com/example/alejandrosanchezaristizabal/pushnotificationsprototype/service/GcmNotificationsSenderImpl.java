package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.Notification;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.model.User;
import com.example.alejandrosanchezaristizabal.pushnotificationsprototype.repository.UserRepository;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Service
public class GcmNotificationsSenderImpl implements GcmNotificationsSender {

  private static final int MAX_RETRIES = 3;
  private static final int MAX_MULTICAST_SIZE = 1000;
  private static final Executor THREAD_POOL = Executors.newFixedThreadPool(5);
  
  private final UserRepository userRepository;
  private Sender gcmSender;
    
  @Autowired
  public GcmNotificationsSenderImpl(UserRepository userRepository, 
    Sender gcmSender) {
    this.userRepository = userRepository;
    this.gcmSender = gcmSender;
  }
  
  /**
   * Checks if the notification is a unicast or a multicast one.
   */
  @Override
  public void sendNotificationToGcm(Notification notification) {
    List<String> registrationIds = getRegistrationIds(notification);
    if (registrationIds.size() == 1) {
      // It's a unicast notification.
      System.out.println("It´s a unicast notification");
      sendSyncNotification(notification, registrationIds.get(0));
    }
    else {
      // It's a multicast notification.
      if (registrationIds != null) {
        System.out.println("It´s a multicast notification");
        sendAsyncNotification(notification, registrationIds);
      }
    }
  }
  
  /**
   * Retrieves the Registration-IDs for the notification.
   */
  public List<String> getRegistrationIds(Notification notification) {
    List<String> registrationIds = new ArrayList<>();
    String[] userIds = notification.getUserIds();
    if (userIds != null) {
      for (int i = 0; i < userIds.length; ++i) {
        // It's necessary to parse the id because it's not a long variable.
        long userId = Long.parseLong(userIds[i]);
        User user = userRepository.findOne(userId);
        if (user != null) registrationIds.add(user.getRegistrationId());
      }
    }
        
    return registrationIds;
  }

  /**
   * Pushes the notification to a single Registration-ID.
   */
  private void sendSyncNotification(Notification notification, 
    String deviceId) {
    final Message message = createMessage(notification);
    try {
      Result result = gcmSender.send(message, deviceId, MAX_RETRIES);
      String messageId = result.getMessageId();
      if (messageId != null) {
        System.out.println("Notification successfully sent: " + result);
      }
      else {
        System.out.println("Failed sending notification: " + result);
      }
    }
    catch (IOException e) {
      System.out.println("Failed sending notification to Device-ID");
      e.printStackTrace();
    }
  }
  
  /**
   * Divides Registration-IDs into subgroups and send the notification to them.
   */
  private void sendAsyncNotification(Notification notification, 
    List<String> deviceIds) {
    final Message message = createMessage(notification);
    if (deviceIds.size() > MAX_MULTICAST_SIZE) {
      List<String> deviceIdsToSend = deviceIds.subList(0, MAX_MULTICAST_SIZE);
      THREAD_POOL
        .execute(new NotificationSenderThread(message, deviceIdsToSend));
      sendAsyncNotification(notification, deviceIds
        .subList(MAX_MULTICAST_SIZE, deviceIds.size()));
    }
    else {
      THREAD_POOL.execute(new NotificationSenderThread(message, deviceIds));
    }
  }
 
  /**
   * Creates a message with the notification's information.
   */
  private Message createMessage(Notification notification) {
    final Message.Builder messageBuilder = new Message.Builder();
    messageBuilder.addData("title", notification.getTitle());
    messageBuilder.addData("body", notification.getBody());
    return messageBuilder.build();
  }
  
  /**
   * Analyzes the result for every try of sending the notification to a
   * particular Device-ID.
   */
  private void analyzeResult(Result result, String deviceId) {
    String messageId = result.getMessageId();
    if (messageId != null) {
      System.out
        .println("Notification successfully sent to Device-ID: " + deviceId 
          + " - " + result);
    }
    else {
      System.out
        .println("Failed sending notification to Device-ID: " + deviceId 
          + " - " + result);
    }
  }
  
  
  private class NotificationSenderThread implements Runnable {

    private final Message message;
    private final List<String> deviceIdsToSend;

    private NotificationSenderThread
      (Message message, List<String> deviceIdsToSend) {
      this.message = message;
      this.deviceIdsToSend = deviceIdsToSend;
    }
    
    /**
     * Pushes the notification to a group of Registration-IDs.
     */
    public void run() {
      MulticastResult multicastResult;
      try {
        multicastResult = gcmSender.send(message, deviceIdsToSend, MAX_RETRIES);
      }
      catch (IOException e) {
        System.out
          .println("Failed sending notification to group of Device-IDs");
        return;
      }
      
      List<Result> results = multicastResult.getResults();
      for (int i = 0; i < deviceIdsToSend.size(); ++i) {
        Result result = results.get(i);
        String deviceId = deviceIdsToSend.get(i);
        analyzeResult(result, deviceId);
      }
    }
  }
}