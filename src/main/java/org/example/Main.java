package org.example;

import org.example.lemon_bot.LemonPaymentBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new LemonPaymentBot());
            System.out.println("boot started");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        ConcurrentHashMap
    }
}