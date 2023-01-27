package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.BotEmoji.*;
public class Bot extends TelegramLongPollingBot {
    private final static BotConfig config = new BotConfig();
    private final static String botUserName = config.getBotUserName();
    private final static String botApiToken = config.getBotApiToken();
    private final Text messagesTexts = new Text(config.getMessagesTexts());
    private final Buttons buttonsTexts = new Buttons(config.getButtonsTexts());
    private final String imagesPath = new String(config.getImagesPath());
    private final Map<Long, Integer> levels = new HashMap<>();
    private boolean courtesy;

    public Bot() throws FileNotFoundException {
    }

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
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (!courtesy) {
            message.setText(fixEncoding("*Привіт! *") + SMILE_EMOJI.getEmoji() + fixEncoding("\nНатисніть /start для початку"));
            message.setParseMode("markdown");
            sendApiMethodAsync(message);
            courtesy = true;
            return;
        }

        if (update.hasMessage()) {
            if ("/start".equals(update.getMessage().getText())) {
                setLevels(chatId, 1);
                sendImage(chatId, "level-1.gif");
                message.setText(messagesTexts.getData().get("level-1.txt"));
                attachButtons(message, buttonsTexts.getButtons("level-1.but", getLevel(chatId)));
            } else {
                message.setText(fixEncoding("Наразі я розумію тільки команду /start ") + MAN_SHRUGGING.getEmoji());
            }
        }

        if (update.hasCallbackQuery()) {
            if (getLevel(chatId) == 5){
                setLevels(chatId, 0);
            }
            if (getLevel(chatId) >= 0) {
                setLevels(chatId, getLevel(chatId) + 1);
                sendImage(chatId, "level-" + getLevel(chatId) + ".gif");
                message.setText(messagesTexts.getData().get("level-" + getLevel(chatId) + ".txt"));
                attachButtons(message, buttonsTexts.getButtons("level-" + getLevel(chatId) + ".but", getLevel(chatId)));
            } else {
                message.setText(fixEncoding("Щось пішло не так ") + FACE_PALM.getEmoji() + fixEncoding(" спробуйте з початку") + "\n/start");
            }
        }
        message.setParseMode("markdown");
        sendApiMethodAsync(message);
    }

    public void attachButtons(SendMessage message, List<Map<String, String>> buttonsPane) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        for (Map<String, String> buttonsRow : buttonsPane) {
            for (String buttonName : buttonsRow.keySet()) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonName);
                button.setCallbackData(buttonsRow.get(buttonName));
                keyboardRow.add(button);
            }
            keyboard.add(keyboardRow);
            keyboardRow = new ArrayList<>();
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
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

    private String fixEncoding(String text) {
        return new String(text.getBytes(), StandardCharsets.UTF_8);
    }

    public void sendImage(Long chatId, String name) {
        SendAnimation animation = new SendAnimation();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File(imagesPath + name));
        animation.setAnimation(inputFile);
        animation.setChatId(chatId);
        executeAsync(animation);
    }

    public Integer getLevel(Long chatId) {
        return levels.getOrDefault(chatId, 0);
    }

    public void setLevels(Long chatId, Integer level) {
        levels.put(chatId, level);
    }
}
