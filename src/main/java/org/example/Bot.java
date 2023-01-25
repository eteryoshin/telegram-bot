package org.example;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private final String smile_emoji = EmojiParser.parseToUnicode(":slight_smile:");
    private final String man_shrugging = "\uD83E\uDD37\u200D\u2642\uFE0F";
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
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (!courtesy) {
            message.setText(fixEncoding("*Привіт! *") + smile_emoji + fixEncoding("\nНатисніть /start для початку"));
            message.setParseMode("markdown");
            sendApiMethodAsync(message);
            courtesy = true;
            return;
        }

        if (update.hasMessage()) {
            switch (update.getMessage().getText()) {
                case ("/start"):
                    message.setText(fixEncoding("Над полониною лунає:"));
                    attachButtons(message, Map.of(
                            "Слава Україні!", "but_1"
                    ));
                    break;
                default:
                    message.setText(fixEncoding("Наразі я розумію тільки команду /start ") + man_shrugging);
            }
        }

        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case "but_1":
                    message = createMessage(chatId, "Героям Слава!");
                    attachButtons(message, Map.of(
                            "Слава Нації", "but_2"
                    ));
                    break;
                case "but_2":
                    message = createMessage(chatId, "Смерть ворогам!");
            }
        }
        sendApiMethodAsync(message);
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonName : buttons.keySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(fixEncoding(buttonName));
            button.setCallbackData(buttons.get(buttonName));
            keyboard.add(List.of(button));
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

    public SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(fixEncoding(text));
        message.setParseMode("markdown");
        message.setChatId(chatId);
        return message;
    }

    private String fixEncoding(String text) {
        return new String(text.getBytes(), StandardCharsets.UTF_8);
    }
}
