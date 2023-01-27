package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Parsable {
    private HashMap<String, String> data = new HashMap<>();

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(this.data);
    }

    public Parsable(String pathname) throws FileNotFoundException {
        fillData(pathname);
    }

    private void fillData(String pathname) throws FileNotFoundException {
        File[] files = new File(pathname).listFiles();
        if (files != null) {
            for (File file : files) {
                StringBuilder message = new StringBuilder();
                Scanner scanner = new Scanner(file);
                scanner.useDelimiter("\r");
                while (scanner.hasNext()) {
                    message.append(new String(scanner.next().getBytes(), StandardCharsets.UTF_8));
                }
                data.put(file.getName(), message.toString());
            }
        }
    }
}
