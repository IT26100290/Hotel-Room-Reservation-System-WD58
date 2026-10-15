
package com.hotel.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String DATA_FOLDER =
            System.getProperty("user.home") + File.separator + "hotel-data" + File.separator;


    public static List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(DATA_FOLDER + fileName);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeAllLines(String fileName, List<String> lines) {
        File file = new File(DATA_FOLDER + fileName);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String fileName, String line) {
        File file = new File(DATA_FOLDER + fileName);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}