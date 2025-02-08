package com.bbstudios.railwaytm;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static final String FILE_NAME = "locations.json"; // Имя JSON-файла

    // Метод для сохранения данных
    public static void saveLocation(Context context, double lat, double lon, String state) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            JSONArray jsonArray;

            // Если файл существует — читаем его, иначе создаем новый JSON
            if (file.exists()) {
                String jsonString = readJsonFromFile(context);
                jsonArray = new JSONArray(jsonString);
            } else {
                jsonArray = new JSONArray();
            }

            // Создаем новый объект
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat", lat);
            jsonObject.put("lon", lon);
            jsonObject.put("state", state);

            // Добавляем в массив
            jsonArray.put(jsonObject);

            // Записываем в файл
            writeJsonToFile(context, jsonArray.toString());

        } catch (JSONException | IOException e) {
            Log.e("JSON_ERROR", "Ошибка при сохранении: " + e.getMessage());
        }
    }

    // Метод для чтения данных
    public static JSONArray readLocations(Context context) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return new JSONArray(); // Если файла нет, возвращаем пустой JSON-массив
            }

            String jsonString = readJsonFromFile(context);
            return new JSONArray(jsonString);

        } catch (JSONException | IOException e) {
            Log.e("JSON_ERROR", "Ошибка при чтении: " + e.getMessage());
            return new JSONArray();
        }
    }

    // Читаем JSON из файла
    private static String readJsonFromFile(Context context) throws IOException {
        File file = new File(context.getFilesDir(), FILE_NAME);
        InputStream is = context.openFileInput(FILE_NAME);
        int size = (int) file.length();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer);
    }

    // Записываем JSON в файл
    private static void writeJsonToFile(Context context, String jsonString) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(context.getFilesDir(), FILE_NAME));
        fileWriter.write(jsonString);
        fileWriter.close();
    }

    // Метод для удаления последней записи из JSON файла
    public static void deleteLastRecord(Context context) {
        File file = new File(context.getFilesDir(), "locations.json");
        if (file.exists()) {
            try {
                // Чтение содержимого файла
                StringBuilder jsonContent = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                // Преобразование в JSONArray
                JSONArray jsonArray = new JSONArray(jsonContent.toString());

                // Удаление последней записи
                if (jsonArray.length() > 0) {
                    jsonArray.remove(jsonArray.length() - 1);
                }

                // Запись обновленного массива обратно в файл
                FileWriter writer = new FileWriter(file);
                writer.write(jsonArray.toString());
                writer.close();

                System.out.println("Последняя запись удалена.");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл не найден.");
        }
    }

    public static void copyJsonFromAssetsToFiles(Context context, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // Открытие файла из assets
            inputStream = context.getAssets().open(fileName);

            // Целевой файл в папке files
            File outFile = new File(context.getFilesDir(), fileName);
            outputStream = new FileOutputStream(outFile);

            // Буфер для чтения и записи данных
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            System.out.println("Файл успешно скопирован в: " + outFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
