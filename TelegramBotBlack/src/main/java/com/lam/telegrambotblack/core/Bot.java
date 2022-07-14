package com.lam.telegrambotblack.core;

import lombok.extern.slf4j.Slf4j;

import com.lam.telegrambotblack.dao.UserDAO;
import com.lam.telegrambotblack.models.User;
import org.hibernate.collection.internal.PersistentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.lam.telegrambotblack.configure.Configure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    public List<String> callbackDataList = new ArrayList<>();
    public UserDAO userDAO;

    @Autowired
    public Bot(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public String getBotUsername() {
        return Configure.getUserName();
    }

    @Override
    public String getBotToken() {
        return Configure.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        if (callbackQuery != null) {
            String data = callbackQuery.getData();

            callbackDataList.add(update.getCallbackQuery().getMessage().getChatId().toString());
            callbackDataList.add(callbackQuery.getData());

        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {

            if (message.getText().equals("/start")) {
                try {
                    welcomeMessage(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            } else if (message.hasText()) {
                try {
                    subscribeToNotifications(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            } else if (message.hasText()) {
                try {
                    welcomeMessage(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void welcomeMessage(Message message) throws TelegramApiException {
        String text = "Добро пожаловать в телеграмм бот по уведомлениям АС ТП."+
                "Вы можете подписаться на уведомления путем\n" +
                "отправки сообщения: subscribe:{code}\n" +
                "Вместо {code} необходимо вставить уникальный код\n" +
                "пользователя jira, который можно найти в личном кабинете\n";
        execute(new SendMessage(message.getChatId().toString(), text));
    }
    public void subscribedMessage(Message message) throws TelegramApiException {
        String text = "Спасибо!\n" +
                "Вы успешно подписались на уведомления!";
        execute(new SendMessage(message.getChatId().toString(), text));
    }
    public void alreadySubscribedMessage(Message message) throws TelegramApiException {
        String text = "Вы уже подписаны на уведомления!";
        execute(new SendMessage(message.getChatId().toString(), text));
    }
    public void subscribeToNotifications(Message message) throws TelegramApiException {
        if (message.getText().startsWith("subscribe:") && message.getText().endsWith("")) {

            String sourceString = message.getText().trim();

            String regex = "\\d+\\S?\\d";
            //pattern compiled
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(sourceString);
            matcher.find();

            String userJiraIDString = matcher.group(0);

            List<User> allUsers = userDAO.getAllUsers();

            if (allUsers.isEmpty()) {

                userDAO.createNewUser(message.getChatId(),
                        Long.parseLong(userJiraIDString), "free");

                subscribedMessage(message);
                return;
            }

            List<User> nextUser = userDAO.getUserByTelegramChatID(message.getChatId());

            if (nextUser.isEmpty()) {
                userDAO.createNewUser(message.getChatId(),
                        Long.parseLong(userJiraIDString), "free");

                subscribedMessage(message);
            } else if (!nextUser.isEmpty()) {
                alreadySubscribedMessage(message);
            }

        }
    }

    public List<String> getCallbackData() {
        return callbackDataList;
    }
}
