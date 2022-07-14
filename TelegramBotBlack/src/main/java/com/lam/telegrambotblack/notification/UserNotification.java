package com.lam.telegrambotblack.notification;


import com.lam.telegrambotblack.models.ListOfUserIDs;
import com.lam.telegrambotblack.models.Notification;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface UserNotification {
    void sendNotification(ListOfUserIDs listUsers, Notification notification) throws TelegramApiException, InterruptedException;
}
