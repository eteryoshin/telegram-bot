package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    private String botUserName;
    private String botApiToken;
    private String messagesTexts;
    private String buttonsTexts;
    private String imagesPath;

    public String getBotUserName() {
        return botUserName;
    }

    public String getBotApiToken() {
        return botApiToken;
    }

    public String getMessagesTexts() {
        return messagesTexts;
    }

    public String getButtonsTexts() {
        return buttonsTexts;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    BotConfig() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("application.properties");
            property.load(fis);

            this.botUserName = property.getProperty("botUserName");
            this.botApiToken = property.getProperty("botApiToken");
            this.messagesTexts = property.getProperty("messagesTexts");
            this.buttonsTexts = property.getProperty("buttonsTexts");
            this.imagesPath = property.getProperty("imagesPath");

        } catch (IOException e) {
            System.err.println("ERROR: File application.properties can't be loaded!");
        }
    }
}

