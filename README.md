# push_notifications_prototype_server
A Proof of Concept for an Android Push Notifications Server.

This app server saves users with their respective Registration-IDs for their Android devices in order to be able to send push notifications to them. There is a service for pushing unicast as well as multicast notifications from this app server (i.e. to a unique device or to a group of devices at the same time). There is also another service to update a user's Registration-ID whenever it has been refreshed by GCM.

### Instructions
* Download this project and the [push_notifications_prototype][push_notif_proto].

* Import both projects into the corresponding IDEs.

* In this project, set the value of the constant server.port (under ${PROJECT_HOME}/src/main/resources/application.properties file) with another port and remove the '#' character if you don't want to work with the default port (i.e. 8080). Something like:

    ```sh
   server.port = 8081
    ```
    > Make sure that you use the same port in the  [push_notifications_prototype][push_notif_proto].

* Run this project first and make sure that it starts successfully before you start playing with the [push_notifications_prototype][push_notif_proto].

* Run the [push_notifications_prototype][push_notif_proto] and only after you see in the screen that your device was successfully registered, you are ready to send push notifications to it.

### Sending a push notification

The service's name for sending a push notification is **"/pushNotification"**.
It should be called when you want to send a notification to one or many devices.

You can use [Postman][postman] or any other REST Client in order to invoke the push-notification service. Your request should be something like:

```
Url: http://YOUR_IP:PORT/pushNotification
Content-Type: application/json
Method: POST
Request Raw Body: 
{
  "title" : "Notification title",
  "body" : "It's working :) !",
  "userIds" : ["1", "2", "3", "7", "223"]
}
```
> Make sure that you use the same IP-address in the [push_notifications_prototype][push_notif_proto].

> For the purpose of this prototype, the User-IDs are going to be auto-incremented positive integers starting with ID = 1. The userIds are sent as Strings so that those identifiers can be easyly extended in the future to more complex ones.

> How to know which userId to use? Well, in theory you could use any positive integer, because the app server is not going to crash if an id doesn't exist. If an id doesn't exist, the notification won't be sent to that particular addressee. But to keep it simple, use any positive integer between "1" and the number of devices you have already registered.

### References
* [GCM Rest Service] - Google Cloud Messaging Rest Service (Visited on April 2016).
* [Sending GCM Notification] - Sending GCM Notification From server – Spring Framework Java (Visited on April 2016).
* [Spring MVC JSON] - Spring MVC JSON (JSON to Java) (Visited on April 2016).
* [Downstream Messaging] - Simple Downstream Messaging (Visited on April 2016).


[//]: # (These are reference links used in the body of this note)
   [push_notif_proto]: <https://github.com/ibalejandro/push_notifications_prototype>
   [postman]: <https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop>
   [GCM Rest Service]: <https://github.com/marlandy/gcm-rest>
   [Sending GCM Notification]: <http://www.devnub.com/sending-gcm-notification-from-server-spring-framework-java/>
   [Spring MVC JSON]: <http://hmkcode.com/spring-mvc-json-json-to-java/>
   [Downstream Messaging]: <https://developers.google.com/cloud-messaging/downstream>
