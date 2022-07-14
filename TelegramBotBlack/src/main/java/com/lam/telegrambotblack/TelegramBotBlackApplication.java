package com.lam.telegrambotblack;

import com.lam.telegrambotblack.core.Bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication
public class TelegramBotBlackApplication {
    private static Bot bot = null;

    @Autowired
    public TelegramBotBlackApplication(Bot bot) {
        this.bot = bot;
    }

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(TelegramBotBlackApplication.class, args);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
	}
}
