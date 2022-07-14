package com.lam.telegrambotblack.notification;

import com.lam.telegrambotblack.core.Bot;
import com.lam.telegrambotblack.models.ListOfUserIDs;
import com.lam.telegrambotblack.models.Notification;
import com.lam.telegrambotblack.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserNotificationImplementation implements UserNotification {
    Thread thread;
    private Bot bot;

    @Autowired
    public UserNotificationImplementation(Bot bot){
        this.bot = bot;
    }
    @Override
    public void sendNotification(ListOfUserIDs listIDs, Notification notification) throws TelegramApiException, InterruptedException {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(bot.getCallbackData() != null && !bot.getCallbackData().isEmpty()) {
                        String eventID = bot.getCallbackData().get(0);
                        String eventStatus = bot.getCallbackData().get(1);
                        bot.getCallbackData().clear();
                        log.info(eventID);
                        log.info(eventStatus);
                        bot.userDAO.updateStatus(Long.parseLong(eventID), eventStatus);
                        try {
                            linkBuutonMessage(eventID);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    for (Long id : listIDs.getTelegramChatIDs()) {
                        log.info("User ID: " + id);
                        if (bot.userDAO.getUserByTelegramChatID(id).isEmpty()){
                            continue;
                        }
                        try {
                            if (bot.userDAO.getUserByTelegramChatID(id).get(0).getStatus().equals("free")) {
                                goToTaskMessage(id.toString(), notification.getType(), notification.getText());
                            } else if (bot.userDAO.getUserByTelegramChatID(id).get(0).getStatus().equals("working")) {
                                continue;
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });

        thread.start();
    }


    public void goToTaskMessage(String telegramChatID, String typeNotification, String textNotification) throws InterruptedException, TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        String text = typeNotification + "\n" + textNotification;
        sendMessage.setText(text);
        sendMessage.setChatId(telegramChatID);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton getLinkButton = new InlineKeyboardButton();
        getLinkButton.setCallbackData("working");
        getLinkButton.setText("Получить ссылку на задачу");

        rowInline.add(getLinkButton);

        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        bot.execute(sendMessage);
    }

    public void linkBuutonMessage(String telegramChatID) throws InterruptedException, TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        String text = "Сообщение";
        sendMessage.setText(text);
        sendMessage.setChatId(telegramChatID);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();


        InlineKeyboardButton goToTaskButton = new InlineKeyboardButton();
        goToTaskButton.setText("Перейти к задаче!");
        goToTaskButton.setUrl("https://jira.softwarecom.ru/secure/Dashboard.jspa");
        rowInline.add(goToTaskButton);

        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        bot.execute(sendMessage);
    }

}

