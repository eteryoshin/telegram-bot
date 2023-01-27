package org.example;

import com.vdurmont.emoji.EmojiParser;

import java.util.HashMap;
import java.util.Map;

public enum BotEmoji {
    SMILE_EMOJI(EmojiParser.parseToUnicode(":slight_smile:")),
    FACE_PALM(EmojiParser.parseToUnicode(":face_palm:")),
    MAN_SHRUGGING("\uD83E\uDD37\u200D\u2642\uFE0F"),
    PIG(EmojiParser.parseToUnicode(":pig:")),
    DOG(EmojiParser.parseToUnicode(":dog:")),
    FLAME(EmojiParser.parseToUnicode(":fire:")),
    UA(EmojiParser.parseToUnicode(":ua:"));

    private final String emoji;

    public String getEmoji() {
        return emoji;
    }

    BotEmoji(String label) {
        this.emoji = label;
    }

}
