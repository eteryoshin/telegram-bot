package org.example;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Buttons extends Parsable {

    public Buttons(String pathname) throws FileNotFoundException {
        super(pathname);
    }

    public List<Map<String, String>> getButtons(String level, Integer levelNumber) {
        String levelData = getData().get(level);
        List<Map<String, String>> buttons = new ArrayList<>();
        if (levelData.length() > 0) {
            String[] levelButtons = getData().get(level).split("\n");
            for (int i = 0; i < levelButtons.length; i++) {
                buttons.add(Map.of(levelButtons[i], "but_" + levelNumber));
            }
        }

        if (buttons.size() >= 3) {
            Collections.shuffle(buttons);
            return buttons.subList(0, 3);
        }
        return buttons;
    }
}

