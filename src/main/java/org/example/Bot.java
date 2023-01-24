package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    private final static String botUserName = "EterGotibot";
    private final static String botApiToken = "5917018861:AAG1Ad-9hUJB2qMAaYPq-PpIIOTvfcfAMFg";
    private boolean courtesy;

    @Override
    public String getBotUsername() {
        return new String(botUserName);
    }

    @Override
    public String getBotToken() {
        return new String(botApiToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        SendMessage message;
        if (!courtesy) {
            message = createMessage("*Hello!*", chatId);
            sendApiMethodAsync(message);
            courtesy = true;
        }
        message = mockingbird(update.getMessage(), chatId);
        sendApiMethodAsync(message);
    }

    public SendMessage mockingbird(Message answer, Long chatId) {
         return createMessage("*Your text is:* " + answer.getText(), chatId);
    }

    public Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }

    public SendMessage createMessage(String str, Long chatId) {
        SendMessage message = new SendMessage();
        message.setText(str);
        message.setParseMode("markdown");
        message.setChatId(chatId);
        return message;
    }
}
