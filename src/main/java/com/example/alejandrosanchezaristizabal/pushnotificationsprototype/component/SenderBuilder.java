package com.example.alejandrosanchezaristizabal.pushnotificationsprototype.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.android.gcm.server.Sender;

@Component 
public class SenderBuilder extends Sender {

  @Autowired
  public SenderBuilder(@Value("${gcmserverkey}") String key) {
    super(key);
  }
}
