package com.lam.telegrambotblack.controllers;

import com.lam.telegrambotblack.models.Notification;
import com.lam.telegrambotblack.notification.UserNotificationImplementation;
import com.lam.telegrambotblack.models.ListOfUserIDs;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RestController
public class BotAPIController {
    @Autowired
    UserNotificationImplementation userNotificationImplementation;

    @RequestMapping(value="/notification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendNotification(@RequestBody com.fasterxml.jackson.databind.@NotNull JsonNode jsonNode) throws TelegramApiException, InterruptedException {
        log.info(this.toString());
        ListOfUserIDs listUsersIDs = new ListOfUserIDs(jsonNode);

        Notification notification = new Notification(jsonNode);

        userNotificationImplementation.sendNotification(listUsersIDs, notification);
    };
}

